package us.craigmiller160.llm.ragpractice.lowlevel.v1.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import us.craigmiller160.llm.ragpractice.common.spring.openai.OpenaiClient
import us.craigmiller160.llm.ragpractice.lowlevel.v1.openai.OpenaiProperties

@Configuration
class OpenaiConfig {
  @Bean
  fun openaiClient(props: OpenaiProperties): OpenaiClient =
      OpenaiClient(baseUrl = props.url, apiKey = props.key)
}
