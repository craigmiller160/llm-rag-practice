package us.craigmiller160.llm.practice.lowlevel.v2

import io.milvus.client.MilvusClient
import io.milvus.client.MilvusServiceClient
import io.milvus.param.ConnectParam
import io.weaviate.client.Config
import io.weaviate.client.WeaviateClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tech.amikos.chromadb.Client
import us.craigmiller160.llm.ragpractice.common.spring.openai.OpenaiClient

@Configuration
class ClientConfig {
  //  @Bean
  fun milvusClient(): MilvusClient {
    val connectParam = ConnectParam.newBuilder().withHost("localhost").withPort(19530).build()
    return MilvusServiceClient(connectParam)
  }

  //  @Bean
  fun chromaClient(): Client = Client("localhost:8000")

  @Bean
  fun weaviateClient(): WeaviateClient {
    val config = Config("http", "localhost:8081")
    return WeaviateClient(config)
  }

  @Bean
  fun openaiClient(@Value("\${openai.key}") apiKey: String): OpenaiClient =
      OpenaiClient(baseUrl = "https://api.openai.com/v1", apiKey = apiKey)
}
