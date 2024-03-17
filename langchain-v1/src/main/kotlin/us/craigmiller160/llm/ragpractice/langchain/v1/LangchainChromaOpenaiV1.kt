package us.craigmiller160.llm.ragpractice.langchain.v1

import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.openai.OpenAiEmbeddingModel
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class LangchainChromaOpenaiV1(@Value("\${openai.key}") private val apiKey: String) {
  private val embeddingStore =
      ChromaEmbeddingStore.builder()
          .baseUrl("http://localhost:8000")
          .collectionName(LangchainChromaExample.COLLECTION_NAME)
          .build()
  private val embeddingModel = OpenAiEmbeddingModel.withApiKey(apiKey)
  private val log = LoggerFactory.getLogger(javaClass)
  @EventListener(ApplicationReadyEvent::class) fun onReady() {}

  private fun embedText(text: String) {
    log.debug("Preparing to embed text: $text")
    val segment = TextSegment.from(text)
    val embeddingResult = embeddingModel.embed(segment)
    embeddingStore.add(embeddingResult.content(), segment)
    log.info("Added embedding to store. Tokens Used: ${embeddingResult.tokenUsage()}")
  }
}
