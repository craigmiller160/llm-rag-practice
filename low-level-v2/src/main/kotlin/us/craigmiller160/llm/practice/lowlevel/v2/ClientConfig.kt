package us.craigmiller160.llm.practice.lowlevel.v2

import io.milvus.client.MilvusClient
import io.milvus.client.MilvusServiceClient
import io.milvus.param.ConnectParam
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import us.craigmiller160.llm.ragpractice.common.spring.openai.OpenaiClient

@Configuration
class ClientConfig {
  @Bean
  fun milvusClient(): MilvusClient {
    val connectParam = ConnectParam.newBuilder().withHost("localhost").withPort(19530).build()
    return MilvusServiceClient(connectParam)
  }

  @Bean
  fun openaiClient(@Value("\${openai.key}") apiKey: String): OpenaiClient =
      OpenaiClient(baseUrl = "https://api.openai.com/v1", apiKey = apiKey)
}
