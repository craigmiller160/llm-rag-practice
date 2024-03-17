package us.craigmiller160.llm.ragpractice.langchain.v1

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.PropertySource
import us.craigmiller160.llm.ragpractice.common.spring.properties.YamlPropertyLoaderFactory

@SpringBootApplication
@PropertySource("file:./secrets.yml", factory = YamlPropertyLoaderFactory::class)
class LangchainV1Runner

fun main(args: Array<String>) {
  SpringApplication.run(LangchainV1Runner::class.java, *args)
}
