package us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "role")
@JsonSubTypes(
    JsonSubTypes.Type(value = SystemMessage::class, name = "system"),
    JsonSubTypes.Type(value = UserMessage::class, name = "user"),
    JsonSubTypes.Type(value = AssistantMessage::class, name = "assistant"),
    JsonSubTypes.Type(value = ToolMessage::class, name = "tool"))
@JsonInclude(JsonInclude.Include.NON_NULL)
sealed interface Message {
  val role: MessageRole
  val content: String
}

data class SystemMessage(override val content: String, val name: String? = null) : Message {
  override val role: MessageRole = MessageRole.SYSTEM
}

data class UserMessage(override val content: String, val name: String? = null) : Message {
  override val role: MessageRole = MessageRole.USER
}

data class AssistantMessage(
    override val content: String,
    val name: String? = null,
    @field:JsonProperty("tool_calls") val toolCalls: List<ToolCall>? = null
) : Message {
  override val role: MessageRole = MessageRole.ASSISTANT
}

data class ToolMessage(
    override val content: String,
    @field:JsonProperty("tool_call_id") val toolCallId: String
) : Message {
  override val role: MessageRole = MessageRole.TOOL
}
