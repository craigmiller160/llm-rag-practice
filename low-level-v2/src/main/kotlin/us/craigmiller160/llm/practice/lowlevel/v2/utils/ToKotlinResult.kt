package us.craigmiller160.llm.practice.lowlevel.v2.utils

import io.weaviate.client.base.Result

fun <T> Result<T>.toKotlinResult(): kotlin.Result<T> {
  if (hasErrors()) {
    return error.messages
        .map { it.throwable }
        .reduce { acc, throwable ->
          acc.addSuppressed(throwable)
          return@reduce acc
        }
        .let { kotlin.Result.failure(it) }
  }
  return kotlin.Result.success(result)
}
