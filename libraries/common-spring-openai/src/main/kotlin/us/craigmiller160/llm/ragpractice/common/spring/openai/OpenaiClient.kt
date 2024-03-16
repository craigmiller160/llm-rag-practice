package us.craigmiller160.llm.ragpractice.common.spring.openai

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.ChatCompletionRequest
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.ChatCompletionResponse
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.CreateEmbeddingRequest
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.CreateEmbeddingResponse

class OpenaiClient(baseUrl: String, apiKey: String) {
  private val restClient: RestClient =
      RestClient.builder()
          .baseUrl(baseUrl)
          .defaultHeader("Authorization", "Bearer $apiKey")
          .requestFactory(HttpComponentsClientHttpRequestFactory())
          .build()

  fun createChatCompletion(request: ChatCompletionRequest): ChatCompletionResponse =
      restClient
          .post()
          .uri("/chat/completions")
          .body(request)
          .retrieve()
          .toEntity(ChatCompletionResponse::class.java)
          .body!!

  fun createEmbedding(request: CreateEmbeddingRequest): CreateEmbeddingResponse =
      restClient
          .post()
          .uri("/embeddings")
          .body(request)
          .retrieve()
          .toEntity(CreateEmbeddingResponse::class.java)
          .body!!
}
