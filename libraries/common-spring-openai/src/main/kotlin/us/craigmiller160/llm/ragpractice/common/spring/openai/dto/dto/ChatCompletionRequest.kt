package us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto

import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.ChatModel
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.Message

data class ChatCompletionRequest(val model: ChatModel, val messages: List<Message>)
