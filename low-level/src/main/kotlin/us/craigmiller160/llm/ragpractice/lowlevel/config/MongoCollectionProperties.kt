package us.craigmiller160.llm.ragpractice.lowlevel.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.data.mongodb.collections")
data class MongoCollectionProperties(val documents: String)
