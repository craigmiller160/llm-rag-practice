package us.craigmiller160.llm.ragpractice.common.milvus.utils

import io.milvus.param.R

fun <T> R<T>.unwrap(): T {
  if (status == R.Status.Success.code) {
    return data
  }
  throw exception
}
