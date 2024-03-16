package us.craigmiller160.llm.ragpractice.langchain.v1

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.PropertySource

@SpringBootApplication @PropertySource(value = ["file:./secret.properties"]) class Runner

fun main(args: Array<String>) {
  SpringApplication.run(Runner::class.java, *args)
}
