package us.craigmiller160.llm.ragpractice.lowlevel.web.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import us.craigmiller160.llm.ragpractice.common.spring.openai.OpenaiClient
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.ChatCompletionRequest
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.UserMessage
import us.craigmiller160.llm.ragpractice.lowlevel.openai.OpenaiProperties
import us.craigmiller160.llm.ragpractice.lowlevel.web.dto.ChatRequest
import us.craigmiller160.llm.ragpractice.lowlevel.web.dto.ChatResponse

@Service
class ChatService(private val client: OpenaiClient, private val props: OpenaiProperties) {
  private val log = LoggerFactory.getLogger(javaClass)
  fun userChat(request: ChatRequest): ChatResponse {
    log.info("Sending chat request: ${request.message}")
    val response =
        UserMessage(content = request.message)
            .let { message ->
              ChatCompletionRequest(model = props.models.parsed.chat, messages = listOf(message))
            }
            .let { client.createChatCompletion(it) }

    log.info("Chat complete. Usage: ${response.usage}")
    val fullContent = response.choices.joinToString("\n") { it.message.content }
    return ChatResponse(message = fullContent)
  }
}
