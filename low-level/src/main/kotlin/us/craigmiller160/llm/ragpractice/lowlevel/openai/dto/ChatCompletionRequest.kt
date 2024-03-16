package us.craigmiller160.llm.ragpractice.lowlevel.openai.dto

import us.craigmiller160.ragpractice.openai.dto.common.ChatModel
import us.craigmiller160.ragpractice.openai.dto.common.Message

data class ChatCompletionRequest(val model: ChatModel, val messages: List<Message>)
