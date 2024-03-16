package us.craigmiller160.llm.ragpractice.lowlevel.web.service

import io.milvus.client.MilvusClient
import io.milvus.grpc.DataType
import io.milvus.param.IndexType
import io.milvus.param.MetricType
import io.milvus.param.collection.CreateCollectionParam
import io.milvus.param.collection.CreateDatabaseParam
import io.milvus.param.collection.FieldType
import io.milvus.param.collection.HasCollectionParam
import io.milvus.param.index.CreateIndexParam
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import us.craigmiller160.ragpractice.config.MilvusProperties
import us.craigmiller160.ragpractice.openai.OpenaiProperties
import us.craigmiller160.ragpractice.utils.rpc.unwrap

@Service
class VectorSetupService(
    private val milvusClient: MilvusClient,
    private val milvusProps: MilvusProperties,
    private val openaiProps: OpenaiProperties
) {
  private val log = LoggerFactory.getLogger(javaClass)
  @EventListener(ApplicationReadyEvent::class)
  fun onReady(event: ApplicationReadyEvent) {
    log.info("Setting up Milvus database & collection")
    milvusClient
        .listDatabases()
        .unwrap()
        .dbNamesList
        .find { it == milvusProps.data.database }
        ?.let { log.debug("Database ${milvusProps.data.database} already exists") }
        ?: createDatabase()

    HasCollectionParam.newBuilder()
        .withDatabaseName(milvusProps.data.database)
        .withCollectionName(milvusProps.data.collection)
        .build()
        .let { milvusClient.hasCollection(it) }
        .unwrap()
        .also {
          when (it) {
            true -> log.debug("Collection ${milvusProps.data.collection} already exists")
            false -> createCollection()
          }
        }
    log.info("Milvus setup complete")
  }

  private fun createCollection() {
    log.debug("Creating collection ${milvusProps.data.collection}")
    val idField =
        FieldType.newBuilder()
            .withName("document_id")
            .withDataType(DataType.Int64)
            .withPrimaryKey(true)
            .withAutoID(true)
            .build()
    val mongoIdField =
        FieldType.newBuilder()
            .withName("document_mongo_id")
            .withDataType(DataType.VarChar)
            .withMaxLength(24)
            .build()
    val documentVectorField =
        FieldType.newBuilder()
            .withName("document_vector")
            .withDataType(DataType.FloatVector)
            .withDimension(openaiProps.models.parsed.embedding.dimensions)
            .build()
    CreateCollectionParam.newBuilder()
        .withDatabaseName(milvusProps.data.database)
        .withCollectionName(milvusProps.data.collection)
        .addFieldType(idField)
        .addFieldType(mongoIdField)
        .addFieldType(documentVectorField)
        .build()
        .also { milvusClient.createCollection(it).unwrap() }

    log.info("Creating collection index")
    CreateIndexParam.newBuilder()
        .withCollectionName(milvusProps.data.collection)
        .withDatabaseName(milvusProps.data.database)
        .withFieldName("document_vector")
        .withIndexType(IndexType.FLAT)
        .withMetricType(MetricType.COSINE)
        //        .withExtraParam("""{"nlist":1024}""")
        .build()
        .also { milvusClient.createIndex(it).unwrap() }
  }

  private fun createDatabase() {
    log.debug("Creating database ${milvusProps.data.database}")
    CreateDatabaseParam.newBuilder().withDatabaseName(milvusProps.data.database).build().also {
      milvusClient.createDatabase(it).unwrap()
    }
  }
}
