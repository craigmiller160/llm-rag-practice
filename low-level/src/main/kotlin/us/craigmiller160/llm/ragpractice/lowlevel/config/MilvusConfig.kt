package us.craigmiller160.llm.ragpractice.lowlevel.config

import io.milvus.client.MilvusClient
import io.milvus.client.MilvusServiceClient
import io.milvus.param.ConnectParam
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MilvusConfig(private val props: MilvusProperties) {
  @Bean
  fun milvusClient(): MilvusClient {
    val connectParam = ConnectParam.newBuilder().withHost(props.host).withPort(props.port).build()
    return MilvusServiceClient(connectParam)
  }
}
