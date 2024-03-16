package us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class Function(val name: String, val arguments: String)

enum class ToolCallType {
  FUNCTION;

  companion object {
    @JsonCreator fun parse(value: String): ToolCallType = ToolCallType.valueOf(value.uppercase())
  }

  @JsonValue val value: String = name.lowercase()
}

data class ToolCall(val id: String, val type: ToolCallType, val function: Function)
