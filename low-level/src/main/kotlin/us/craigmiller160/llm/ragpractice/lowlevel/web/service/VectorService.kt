package us.craigmiller160.llm.ragpractice.lowlevel.web.service

import io.milvus.client.MilvusClient
import io.milvus.common.clientenum.ConsistencyLevelEnum
import io.milvus.param.MetricType
import io.milvus.param.collection.LoadCollectionParam
import io.milvus.param.collection.ReleaseCollectionParam
import io.milvus.param.dml.InsertParam
import io.milvus.param.dml.SearchParam
import org.bson.Document
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import us.craigmiller160.llm.ragpractice.lowlevel.config.MilvusProperties
import us.craigmiller160.llm.ragpractice.lowlevel.config.MongoCollectionProperties

@Service
class VectorService(
    private val milvusClient: MilvusClient,
    private val milvusProps: MilvusProperties,
    private val mongoTemplate: MongoTemplate,
    private val mongoCollectionProps: MongoCollectionProperties,
    private val openaiClient: OpenaiClient
) {
  private val log = LoggerFactory.getLogger(javaClass)

  fun search(request: SearchRequest) {
    log.info("Performing search")
    val start = System.nanoTime()
    val embeddingResponse = openaiClient.createEmbedding(request.query)
    val vectors = embeddingResponse.data.map { it.embedding }

    try {
      LoadCollectionParam.newBuilder()
          .withDatabaseName(milvusProps.data.database)
          .withCollectionName(milvusProps.data.collection)
          .build()
          .also { milvusClient.loadCollection(it).unwrap() }

      val results =
          SearchParam.newBuilder()
              .withCollectionName(milvusProps.data.collection)
              .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
              .withMetricType(MetricType.COSINE)
              .withOutFields(listOf("document_mongo_id", "document_vector"))
              .withVectorFieldName("document_vector")
              .withVectors(vectors)
              //              .withParams("""{"nprobe":10, "offset":0}""")
              .withTopK(10)
              .build()
              .let { milvusClient.search(it).unwrap() }

      val end = System.nanoTime()
      log.info("Search complete: Duration: ${end - start}ns")
      println(results)
    } finally {
      ReleaseCollectionParam.newBuilder()
          .withCollectionName(milvusProps.data.collection)
          .build()
          .also { milvusClient.releaseCollection(it).unwrap() }
    }
  }

  fun storeDocument(request: StoreDocumentRequest): StoreDocumentResponse {
    log.info("Storing document")
    val docId =
        Document()
            .append("content", request.document)
            .let { mongoTemplate.save(it, mongoCollectionProps.documents) }
            .getObjectId("_id")
            .toHexString()
    val embeddingResponse = openaiClient.createEmbedding(request.document)

    val results =
        embeddingResponse.data
            .map { it.embedding }
            .map { embedding ->
              listOf(
                      InsertParam.Field("document_mongo_id", listOf(docId)),
                      InsertParam.Field("document_vector", listOf(embedding)))
                  .let { fields ->
                    InsertParam.newBuilder()
                        .withCollectionName(milvusProps.data.collection)
                        .withDatabaseName(milvusProps.data.database)
                        .withFields(fields)
                        .build()
                  }
            }
            .map { milvusClient.insert(it) }
            .map { it.unwrap() }
    println(results)

    return StoreDocumentResponse(mongoId = docId, milvusIds = listOf())
  }
}
