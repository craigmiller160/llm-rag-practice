package us.craigmiller160.llm.ragpractice.lowlevel.utils.rpc

import io.milvus.param.R

fun <T> R<T>.unwrap(): T {
  if (status == R.Status.Success.code) {
    return data
  }
  throw exception
}
