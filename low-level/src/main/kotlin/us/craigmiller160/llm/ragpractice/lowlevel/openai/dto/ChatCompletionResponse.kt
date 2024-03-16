package us.craigmiller160.llm.ragpractice.lowlevel.openai.dto

import com.fasterxml.jackson.annotation.JsonProperty
import us.craigmiller160.llm.ragpractice.lowlevel.openai.dto.common.ChatModel
import us.craigmiller160.llm.ragpractice.lowlevel.openai.dto.common.FinishReason
import us.craigmiller160.llm.ragpractice.lowlevel.openai.dto.common.Message
import us.craigmiller160.llm.ragpractice.lowlevel.openai.dto.common.ResponseObject
import us.craigmiller160.llm.ragpractice.lowlevel.openai.dto.common.Usage

data class Choice(
    val index: Int,
    val message: Message,
    val logprobs: Boolean?,
    @field:JsonProperty("finish_reason") val finishReason: FinishReason
)

data class ChatCompletionResponse(
    val id: String,
    val `object`: ResponseObject,
    val created: Long,
    val model: ChatModel,
    @field:JsonProperty("system_fingerprint") val systemFingerprint: String,
    val choices: List<Choice>,
    val usage: Usage
)
