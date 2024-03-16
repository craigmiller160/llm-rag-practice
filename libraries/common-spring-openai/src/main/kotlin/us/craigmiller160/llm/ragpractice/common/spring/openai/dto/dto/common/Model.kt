package us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class ChatModel(@JsonValue val value: String) {
  GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125"),
  GPT_3_5_TURBO_INSTRUCT("gpt-3.5-turbo-instruct"),
  GPT_4("gpt-4"),
  GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview");

  companion object {
    @JsonCreator
    fun parse(value: String): ChatModel =
        values().find { it.value == value }
            ?: throw IllegalArgumentException("No chat model exists with value: $value")
  }
}

enum class EmbeddingModel(@JsonValue val value: String, val dimensions: Int) {
  TEXT_EMBEDDING_3_SMALL("text-embedding-3-small", 1536),
  TEXT_EMBEDDING_3_LARGE("text-embedding-3-large", 3072);

  companion object {
    @JsonCreator
    fun parse(value: String): EmbeddingModel =
        values().find { it.value == value }
            ?: throw IllegalArgumentException("No embedding model exists with value: $value")
  }
}
