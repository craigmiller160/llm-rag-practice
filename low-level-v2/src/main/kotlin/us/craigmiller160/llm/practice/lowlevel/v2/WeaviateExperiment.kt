package us.craigmiller160.llm.practice.lowlevel.v2

import io.weaviate.client.WeaviateClient
import io.weaviate.client.v1.graphql.query.argument.NearVectorArgument
import io.weaviate.client.v1.graphql.query.fields.Field
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import us.craigmiller160.llm.practice.lowlevel.v2.utils.toKotlinResult
import us.craigmiller160.llm.ragpractice.common.spring.openai.OpenaiClient
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.ChatCompletionRequest
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.CreateEmbeddingRequest
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.ChatModel
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.EmbeddingModel
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.UserMessage

@Component
class WeaviateExperiment(
    private val weaviateClient: WeaviateClient,
    private val openaiClient: OpenaiClient
) {
  companion object {
    const val CLASS_NAME = "Test"
    const val TEXT_FIELD = "text"
  }
  private val log = LoggerFactory.getLogger(javaClass)
  @EventListener(ApplicationReadyEvent::class)
  fun onReady() {
    //    log.info("Creating class")
    //    WeaviateClass.builder()
    //        .className(CLASS_NAME)
    //        .properties(
    //
    // listOf(Property.builder().name(TEXT_FIELD).dataType(listOf(DataType.TEXT)).build()))
    //        .build()
    //        .let { weaviateClient.schema().classCreator().withClass(it).run() }
    //        .toKotlinResult()
    //        .getOrThrow()
    //    log.info("Class creation complete")
    //
    //    log.info("Creating embeddings")
    //    val text1 = "I like football."
    //    val embedding1 =
    //        CreateEmbeddingRequest(model = EmbeddingModel.TEXT_EMBEDDING_3_SMALL, input = text1)
    //            .let { openaiClient.createEmbedding(it) }
    //            .data
    //            .flatMap { it.embedding }
    //
    //    val text2 = "The weather is good today."
    //    val embedding2 =
    //        CreateEmbeddingRequest(model = EmbeddingModel.TEXT_EMBEDDING_3_SMALL, input = text2)
    //            .let { openaiClient.createEmbedding(it) }
    //            .data
    //            .flatMap { it.embedding }
    //    log.info("Embeddings created")
    //
    //    log.info("Writing documents and embeddings to store")
    //    weaviateClient
    //        .data()
    //        .creator()
    //        .withClassName(CLASS_NAME)
    //        .withID(UUID.randomUUID().toString()) // TODO can this be auto-generated?
    //        .withProperties(mapOf(TEXT_FIELD to text1))
    //        .withVector(embedding1.toTypedArray())
    //        .run()
    //        .toKotlinResult()
    //        .getOrThrow()
    //
    //    weaviateClient
    //        .data()
    //        .creator()
    //        .withClassName(CLASS_NAME)
    //        .withID(UUID.randomUUID().toString()) // TODO can this be auto-generated?
    //        .withProperties(mapOf(TEXT_FIELD to text2))
    //        .withVector(embedding2.toTypedArray())
    //        .run()
    //        .toKotlinResult()
    //        .getOrThrow()
    //    log.info("Documents and embeddings written to store")

    log.info("Creating search query embedding")
    val query = "What is your favorite sport?"
    val searchEmbedding =
        CreateEmbeddingRequest(model = EmbeddingModel.TEXT_EMBEDDING_3_SMALL, input = query)
            .let { openaiClient.createEmbedding(it) }
            .data
            .flatMap { it.embedding }
    log.info("Search query embedding created")

    log.info("Performing search")
    val results =
        weaviateClient
            .graphQL()
            .get()
            .withClassName(CLASS_NAME)
            .withFields(Field.builder().name(TEXT_FIELD).build())
            .withNearVector(
                NearVectorArgument.builder().vector(searchEmbedding.toTypedArray()).build())
            .withLimit(1)
            .run()
            .toKotlinResult()
            .getOrThrow()
    log.info("Search completed")

    log.info("Results: {}", results.data)
    log.info("Results Type: {}", results.data.javaClass.name)

    val textList =
        getMapValue(results.data as Map<String, Any>, "Get").let { getMapValue(it, CLASS_NAME) }
            as List<Map<String, String>>

    ChatCompletionRequest(
            model = ChatModel.GPT_3_5_TURBO_0125,
            messages =
                listOf(
                    UserMessage(
                        content =
                            """
                  Answer this question: $query
                  
                  Using this data:
                  ${textList.joinToString("\n")}
              """
                                .trimIndent())))
        .let { openaiClient.createChatCompletion(it) }
        .choices
        .forEach { log.info("Response: ${it.message.content}") }
  }

  private fun getMapValue(data: Any?, key: String): Any? =
      data?.let { (it as Map<String, Any>)[key] }
}
