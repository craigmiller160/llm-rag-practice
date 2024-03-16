package us.craigmiller160.llm.ragpractice.lowlevel.web.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import us.craigmiller160.ragpractice.web.dto.ChatRequest
import us.craigmiller160.ragpractice.web.dto.ChatResponse
import us.craigmiller160.ragpractice.web.service.ChatService

@RestController
@RequestMapping("/chat")
class ChatController(private val chatService: ChatService) {

  @PostMapping("/user")
  fun userChat(@RequestBody chatRequest: ChatRequest): ChatResponse =
      chatService.userChat(chatRequest)
}
