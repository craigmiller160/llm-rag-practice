package us.craigmiller160.llm.practice.lowlevel.v2

import io.weaviate.client.WeaviateClient
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import us.craigmiller160.llm.ragpractice.common.spring.openai.OpenaiClient

@Component
class WeaviateExperiment(
    private val weaviateClient: WeaviateClient,
    private val openaiClient: OpenaiClient
) {
  private val log = LoggerFactory.getLogger(javaClass)
  @EventListener(ApplicationReadyEvent::class) fun onReady() {}
}
