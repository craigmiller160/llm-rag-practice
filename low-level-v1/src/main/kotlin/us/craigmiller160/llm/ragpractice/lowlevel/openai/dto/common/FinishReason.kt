package us.craigmiller160.llm.ragpractice.lowlevel.openai.dto.common

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class FinishReason {
  STOP,
  LENGTH,
  CONTENT_FILTER,
  TOOL_CALLS;

  companion object {
    @JsonCreator fun parse(value: String): FinishReason = FinishReason.valueOf(value.uppercase())
  }

  @JsonValue val value: String = name.lowercase()
}
