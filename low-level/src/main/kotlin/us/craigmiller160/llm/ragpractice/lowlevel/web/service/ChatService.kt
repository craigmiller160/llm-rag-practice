package us.craigmiller160.llm.ragpractice.lowlevel.web.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import us.craigmiller160.ragpractice.openai.OpenaiClient
import us.craigmiller160.ragpractice.openai.dto.common.UserMessage
import us.craigmiller160.ragpractice.web.dto.ChatRequest
import us.craigmiller160.ragpractice.web.dto.ChatResponse

@Service
class ChatService(private val client: OpenaiClient) {
  private val log = LoggerFactory.getLogger(javaClass)
  fun userChat(request: ChatRequest): ChatResponse {
    log.info("Sending chat request: ${request.message}")
    val message = UserMessage(content = request.message)
    val response = client.createChatCompletion(listOf(message))
    log.info("Chat complete. Usage: ${response.usage}")
    val fullContent = response.choices.joinToString("\n") { it.message.content }
    return ChatResponse(message = fullContent)
  }
}
