package us.craigmiller160.llm.practice.lowlevel.v2

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.PropertySource
import us.craigmiller160.llm.ragpractice.common.spring.properties.YamlPropertyLoaderFactory

@SpringBootApplication
@PropertySource("file:./secrets.yml", factory = YamlPropertyLoaderFactory::class)
class LowLevelV2Runner

fun main(args: Array<String>) {
  SpringApplication.run(LowLevelV2Runner::class.java, *args)
}
