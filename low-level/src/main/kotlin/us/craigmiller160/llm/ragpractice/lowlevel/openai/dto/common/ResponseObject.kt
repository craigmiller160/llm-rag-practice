package us.craigmiller160.llm.ragpractice.lowlevel.openai.dto.common

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class ResponseObject(@JsonValue val value: String) {
  LIST("list"),
  EMBEDDING("embedding"),
  CHAT_COMPLETION("chat.completion");

  companion object {
    @JsonCreator
    fun parse(value: String): ResponseObject =
        values().find { it.value == value }
            ?: throw IllegalArgumentException("Unable to find response object: $value")
  }
}
