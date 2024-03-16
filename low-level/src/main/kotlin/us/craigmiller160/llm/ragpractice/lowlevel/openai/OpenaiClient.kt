package us.craigmiller160.llm.ragpractice.lowlevel.openai

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import us.craigmiller160.ragpractice.openai.dto.ChatCompletionRequest
import us.craigmiller160.ragpractice.openai.dto.ChatCompletionResponse
import us.craigmiller160.ragpractice.openai.dto.CreateEmbeddingRequest
import us.craigmiller160.ragpractice.openai.dto.CreateEmbeddingResponse
import us.craigmiller160.ragpractice.openai.dto.common.Message

@Component
class OpenaiClient(private val props: OpenaiProperties) {
  private val restClient: RestClient =
      RestClient.builder()
          .baseUrl(props.url)
          .defaultHeader("Authorization", "Bearer ${props.key}")
          .requestFactory(HttpComponentsClientHttpRequestFactory())
          .build()

  fun createChatCompletion(messages: List<Message>): ChatCompletionResponse {
    val request = ChatCompletionRequest(model = props.models.parsed.chat, messages = messages)
    return restClient
        .post()
        .uri("/chat/completions")
        .body(request)
        .retrieve()
        .toEntity(ChatCompletionResponse::class.java)
        .body!!
  }

  fun createEmbedding(text: String): CreateEmbeddingResponse {
    val request = CreateEmbeddingRequest(model = props.models.parsed.embedding, input = text)
    return restClient
        .post()
        .uri("/embeddings")
        .body(request)
        .retrieve()
        .toEntity(CreateEmbeddingResponse::class.java)
        .body!!
  }
}
