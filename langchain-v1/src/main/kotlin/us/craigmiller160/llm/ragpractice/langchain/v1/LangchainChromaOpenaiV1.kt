package us.craigmiller160.llm.ragpractice.langchain.v1

import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class LangchainChromaOpenaiV1 {
  private val embeddingStore =
      ChromaEmbeddingStore.builder()
          .baseUrl("http://localhost:8000")
          .collectionName(LangchainChromaExample.COLLECTION_NAME)
          .build()
  @EventListener(ApplicationReadyEvent::class) fun onReady() {}

  private fun embedText(text: String) {
    val segment = TextSegment.from(text)
    TODO()
  }
}
