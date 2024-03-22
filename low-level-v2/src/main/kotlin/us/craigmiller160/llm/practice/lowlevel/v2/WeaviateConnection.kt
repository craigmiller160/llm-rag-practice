package us.craigmiller160.llm.practice.lowlevel.v2

import io.weaviate.client.Config
import io.weaviate.client.WeaviateAuthClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import us.craigmiller160.llm.practice.lowlevel.v2.utils.toKotlinResult

@Component
class WeaviateConnection(@Value("\${weaviate.key}") private val weaviateKey: String) {
  @EventListener(ApplicationReadyEvent::class)
  fun onReady() {
    val config = Config("https", "craigpc:30000")
    val client = WeaviateAuthClient.apiKey(config, weaviateKey)

    val result = client.schema().getter().run().toKotlinResult().getOrThrow()
    println(result)
  }
}
