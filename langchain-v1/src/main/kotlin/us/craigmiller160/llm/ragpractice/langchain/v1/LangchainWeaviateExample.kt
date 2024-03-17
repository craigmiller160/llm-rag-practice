package us.craigmiller160.llm.ragpractice.langchain.v1

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class LangchainWeaviateExample {
  @EventListener(ApplicationReadyEvent::class) fun onReady() {}
}
