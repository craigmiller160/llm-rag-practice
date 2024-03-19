package us.craigmiller160.llm.practice.lowlevel.v2.utils

import io.weaviate.client.base.Result

fun <T> Result<T>.toKotlinResult(): kotlin.Result<T> {
  if (hasErrors()) {
    val combinedMessages = error.messages.joinToString(",") { it.message }
    return kotlin.Result.failure(RuntimeException("Weaviate error: $combinedMessages"))
  }
  return kotlin.Result.success(result)
}
