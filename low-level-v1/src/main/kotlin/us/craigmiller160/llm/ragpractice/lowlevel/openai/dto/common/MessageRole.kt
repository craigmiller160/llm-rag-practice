package us.craigmiller160.llm.ragpractice.lowlevel.openai.dto.common

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class MessageRole {
  SYSTEM,
  USER,
  ASSISTANT,
  TOOL;

  companion object {
    @JsonCreator fun parse(value: String): MessageRole = MessageRole.parse(value.uppercase())
  }

  @JsonValue val value: String = name.lowercase()
}
