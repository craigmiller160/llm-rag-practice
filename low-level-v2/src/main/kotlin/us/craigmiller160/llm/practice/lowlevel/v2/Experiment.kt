package us.craigmiller160.llm.practice.lowlevel.v2

import com.alibaba.fastjson.JSONObject
import io.milvus.client.MilvusClient
import io.milvus.grpc.DataType
import io.milvus.param.collection.CreateCollectionParam
import io.milvus.param.collection.FieldType
import io.milvus.param.dml.InsertParam
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import us.craigmiller160.llm.ragpractice.common.milvus.utils.unwrap
import us.craigmiller160.llm.ragpractice.common.spring.openai.OpenaiClient
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.CreateEmbeddingRequest
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.EmbeddingModel

@Component
class Experiment(private val milvusClient: MilvusClient, private val openaiClient: OpenaiClient) {
  companion object {
    const val COLLECTION_NAME = "my_collection"
    const val ID_FIELD_NAME = "id"
    const val TEXT_FIELD_NAME = "text"
    const val METADATA_FIELD_NAME = "metadata"
    const val VECTOR_FIELD_NAME = "vector"
  }

  private val log = LoggerFactory.getLogger(javaClass)
  @EventListener(ApplicationReadyEvent::class)
  fun onReady() {
    setupCollection()
    setupDocuments()
  }

  private fun setupDocuments() {
    val text1 = "I like football."
    val embedding1 =
        CreateEmbeddingRequest(model = EmbeddingModel.TEXT_EMBEDDING_3_SMALL, input = text1)
            .let { openaiClient.createEmbedding(it) }
            .data
            .map { it.embedding }

    val text2 = "The weather is good today."
    val embedding2 =
        CreateEmbeddingRequest(model = EmbeddingModel.TEXT_EMBEDDING_3_SMALL, input = text2)
            .let { openaiClient.createEmbedding(it) }
            .data
            .map { it.embedding }

    listOf(
            InsertParam.Field(
                ID_FIELD_NAME, listOf(UUID.randomUUID().toString(), UUID.randomUUID().toString())),
            InsertParam.Field(TEXT_FIELD_NAME, listOf(text1, text2)),
            InsertParam.Field(METADATA_FIELD_NAME, listOf(JSONObject(), JSONObject())),
            InsertParam.Field(VECTOR_FIELD_NAME, listOf(embedding1, embedding2)))
        .let { fields ->
          InsertParam.newBuilder().withCollectionName(COLLECTION_NAME).withFields(fields).build()
        }
        .let { milvusClient.insert(it) }
        .unwrap()
  }

  private fun setupCollection() {
    log.info("Setting up collection")
    val idField =
        FieldType.newBuilder()
            .withName(ID_FIELD_NAME)
            .withDataType(DataType.VarChar)
            .withMaxLength(36)
            .withPrimaryKey(true)
            .withAutoID(false)
            .build()

    val textField =
        FieldType.newBuilder()
            .withName(TEXT_FIELD_NAME)
            .withDataType(DataType.VarChar)
            .withMaxLength(65535)
            .build()

    val metadataField =
        FieldType.newBuilder().withName(METADATA_FIELD_NAME).withDataType(DataType.JSON).build()

    val vectorField =
        FieldType.newBuilder()
            .withName(VECTOR_FIELD_NAME)
            .withDataType(DataType.FloatVector)
            .withDimension(EmbeddingModel.TEXT_EMBEDDING_3_SMALL.dimensions)
            .build()

    CreateCollectionParam.newBuilder()
        .withCollectionName(COLLECTION_NAME)
        .addFieldType(idField)
        .addFieldType(textField)
        .addFieldType(metadataField)
        .addFieldType(vectorField)
        .build()
        .let { milvusClient.createCollection(it) }
        .unwrap()
    log.info("Collection is setup")
  }
}
