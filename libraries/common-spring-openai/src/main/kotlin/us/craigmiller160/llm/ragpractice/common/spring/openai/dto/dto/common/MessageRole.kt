package us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class MessageRole {
  SYSTEM,
  USER,
  ASSISTANT,
  TOOL;

  companion object {
    @JsonCreator fun parse(value: String): MessageRole = parse(value.uppercase())
  }

  @JsonValue val value: String = name.lowercase()
}
