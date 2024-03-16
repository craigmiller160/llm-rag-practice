package us.craigmiller160.llm.practice.lowlevel.v2

import io.milvus.client.MilvusClient
import io.milvus.grpc.DataType
import io.milvus.param.collection.CreateCollectionParam
import io.milvus.param.collection.FieldType
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import us.craigmiller160.llm.ragpractice.common.milvus.utils.unwrap
import us.craigmiller160.llm.ragpractice.common.spring.openai.OpenaiClient
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
  @EventListener(ApplicationReadyEvent::class)
  fun onReady() {
    setupCollection()
  }

  private fun setupCollection() {
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
  }
}
