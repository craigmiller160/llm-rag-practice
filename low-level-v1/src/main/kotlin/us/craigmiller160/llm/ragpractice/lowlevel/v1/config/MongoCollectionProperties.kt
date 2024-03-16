package us.craigmiller160.llm.ragpractice.lowlevel.v1.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.data.mongodb.collections")
data class MongoCollectionProperties(val documents: String)
