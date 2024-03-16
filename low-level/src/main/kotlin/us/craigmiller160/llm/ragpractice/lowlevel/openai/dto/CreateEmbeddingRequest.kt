package us.craigmiller160.llm.ragpractice.lowlevel.openai.dto

import us.craigmiller160.ragpractice.openai.dto.common.EmbeddingModel

data class CreateEmbeddingRequest(val model: EmbeddingModel, val input: String)
