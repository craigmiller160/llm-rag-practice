package us.craigmiller160.llm.practice.lowlevel.v2

import io.weaviate.client.WeaviateClient
import io.weaviate.client.v1.schema.model.DataType
import io.weaviate.client.v1.schema.model.Property
import io.weaviate.client.v1.schema.model.WeaviateClass
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import us.craigmiller160.llm.practice.lowlevel.v2.utils.toKotlinResult
import us.craigmiller160.llm.ragpractice.common.spring.openai.OpenaiClient

@Component
class WeaviateExperiment(
    private val weaviateClient: WeaviateClient,
    private val openaiClient: OpenaiClient
) {
  companion object {
    const val CLASS_NAME = "Test"
  }
  private val log = LoggerFactory.getLogger(javaClass)
  @EventListener(ApplicationReadyEvent::class)
  fun onReady() {
    WeaviateClass.builder()
        .className(CLASS_NAME)
        .properties(listOf(Property.builder().name("text").dataType(listOf(DataType.TEXT)).build()))
        .build()
        .let { weaviateClient.schema().classCreator().withClass(it).run() }
        .toKotlinResult()
        .getOrThrow()
  }
}
