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
import us.craigmiller160.llm.ragpractice.common.milvus.utils.unwrap
import us.craigmiller160.llm.ragpractice.common.spring.openai.OpenaiClient
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.CreateEmbeddingRequest
import us.craigmiller160.llm.ragpractice.lowlevel.config.MilvusProperties
import us.craigmiller160.llm.ragpractice.lowlevel.config.MongoCollectionProperties
import us.craigmiller160.llm.ragpractice.lowlevel.openai.OpenaiProperties
import us.craigmiller160.llm.ragpractice.lowlevel.web.dto.SearchRequest
import us.craigmiller160.llm.ragpractice.lowlevel.web.dto.StoreDocumentRequest
import us.craigmiller160.llm.ragpractice.lowlevel.web.dto.StoreDocumentResponse

@Service
class VectorService(
    private val milvusClient: MilvusClient,
    private val milvusProps: MilvusProperties,
    private val mongoTemplate: MongoTemplate,
    private val mongoCollectionProps: MongoCollectionProperties,
    private val openaiClient: OpenaiClient,
    private val openaiProps: OpenaiProperties
) {
  private val log = LoggerFactory.getLogger(javaClass)

  fun search(request: SearchRequest) {
    log.info("Performing search")
    val start = System.nanoTime()
    val queryVectors =
        CreateEmbeddingRequest(model = openaiProps.models.parsed.embedding, input = request.query)
            .let { openaiClient.createEmbedding(it) }
            .data
            .map { it.embedding }

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
              .withVectors(queryVectors)
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
    val results =
        CreateEmbeddingRequest(
                model = openaiProps.models.parsed.embedding, input = request.document)
            .let { openaiClient.createEmbedding(it) }
            .data
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
