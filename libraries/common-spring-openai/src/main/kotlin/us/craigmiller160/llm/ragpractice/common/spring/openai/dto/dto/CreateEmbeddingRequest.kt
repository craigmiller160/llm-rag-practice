package us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto

import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.EmbeddingModel

data class CreateEmbeddingRequest(val model: EmbeddingModel, val input: String)
