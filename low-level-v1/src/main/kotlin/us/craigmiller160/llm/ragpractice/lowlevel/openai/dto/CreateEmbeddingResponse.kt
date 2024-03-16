package us.craigmiller160.llm.ragpractice.lowlevel.openai.dto

import us.craigmiller160.llm.ragpractice.lowlevel.openai.dto.common.ResponseObject

data class EmbeddingRecord(
    val `object`: ResponseObject,
    val index: Int,
    val embedding: List<Float>
)

data class CreateEmbeddingResponse(val `object`: ResponseObject, val data: List<EmbeddingRecord>)
