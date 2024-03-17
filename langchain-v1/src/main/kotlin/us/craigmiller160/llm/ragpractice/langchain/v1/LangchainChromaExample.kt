package us.craigmiller160.llm.ragpractice.langchain.v1

import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener

// @Component
class LangchainChromaExample {
  companion object {
    const val COLLECTION_NAME = "my_collection"
  }
  @EventListener(ApplicationReadyEvent::class)
  fun onReady() {
    val embeddingStore =
        ChromaEmbeddingStore.builder()
            .baseUrl("http://localhost:8000")
            .collectionName(COLLECTION_NAME)
            .build()

    val embeddingModel = AllMiniLmL6V2EmbeddingModel()

    println("Preparing first embedding")
    val segment1 = TextSegment.from("I like football.")
    val embedding1 =
        embeddingModel.embed(segment1).also { println("EMBEDDING 1: ${it.tokenUsage()}") }.content()
    embeddingStore.add(embedding1, segment1)

    println("Preparing second embedding")
    val segment2 = TextSegment.from("The weather is good today.")
    val embedding2 =
        embeddingModel.embed(segment2).also { println("EMBEDDING 2: ${it.tokenUsage()}") }.content()
    embeddingStore.add(embedding2, segment2)

    println("Preparing query embedding")
    val queryEmbedding =
        embeddingModel
            .embed("What is your favorite sport?")
            .also { println("QUERY: ${it.tokenUsage()}") }
            .content()

    println("Performing query")
    val relevantMatches = embeddingStore.findRelevant(queryEmbedding, 1)
    println("Found match")
    val match = relevantMatches.first()

    println("SCORE: ${match.score()}")
    println("TEXT: ${match.embedded().text()}")
  }
}
