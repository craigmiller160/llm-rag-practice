package us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto

import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.ResponseObject

data class EmbeddingRecord(
    val `object`: ResponseObject,
    val index: Int,
    val embedding: List<Float>
)

data class CreateEmbeddingResponse(val `object`: ResponseObject, val data: List<EmbeddingRecord>)
