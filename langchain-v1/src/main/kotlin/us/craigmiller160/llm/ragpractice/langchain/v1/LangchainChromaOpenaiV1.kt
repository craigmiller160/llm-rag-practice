package us.craigmiller160.llm.ragpractice.langchain.v1

import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.openai.OpenAiEmbeddingModel
import dev.langchain4j.store.embedding.EmbeddingMatch
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
  @EventListener(ApplicationReadyEvent::class)
  fun onReady() {
    embedText("I like football.")
    embedText("The weather is good today.")
    val match =
        createQueryEmbedding("What is your favorite sport?").let { performQuery(it) }.first()

    log.info("Match Found. Score: ${match.score()} Text: ${match.embedded().text()}")
  }

  private fun performQuery(embedding: Embedding): List<EmbeddingMatch<TextSegment>> {
    log.debug("Preparing for perform query")
    return embeddingStore.findRelevant(embedding, 1)
  }

  private fun createQueryEmbedding(text: String): Embedding {
    log.debug("Preparing to create query text embedding: $text")
    val segment = TextSegment.from(text)
    val embeddingResult = embeddingModel.embed(segment)
    log.info("Prepared query text embedding. Tokens Used: ${embeddingResult.tokenUsage()}")
    return embeddingResult.content()
  }

  private fun embedText(text: String) {
    log.debug("Preparing to store text embedding: $text")
    val segment = TextSegment.from(text)
    val embeddingResult = embeddingModel.embed(segment)
    embeddingStore.add(embeddingResult.content(), segment)
    log.info("Added text embedding to store. Tokens Used: ${embeddingResult.tokenUsage()}")
  }
}
