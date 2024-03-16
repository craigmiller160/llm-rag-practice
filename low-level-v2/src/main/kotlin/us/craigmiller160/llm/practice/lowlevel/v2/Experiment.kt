package us.craigmiller160.llm.practice.lowlevel.v2

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class Experiment {
  private @EventListener(ApplicationReadyEvent::class) fun onReady() {
    setupCollection()
  }

  private fun setupCollection() {}
}
