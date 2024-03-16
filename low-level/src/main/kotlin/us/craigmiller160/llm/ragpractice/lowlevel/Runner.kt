package us.craigmiller160.llm.ragpractice.lowlevel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import us.craigmiller160.llm.ragpractice.common.spring.properties.YamlPropertyLoaderFactory

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["us.craigmiller160.llm.ragpractice.lowlevel"])
@PropertySource("file:../secrets.yml", factory = YamlPropertyLoaderFactory::class)
class Runner

fun main(args: Array<String>) {
  runApplication<Runner>(*args)
}
