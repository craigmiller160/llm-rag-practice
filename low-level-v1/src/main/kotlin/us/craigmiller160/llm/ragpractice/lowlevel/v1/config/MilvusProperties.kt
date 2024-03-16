package us.craigmiller160.llm.ragpractice.lowlevel.v1.config

import org.springframework.boot.context.properties.ConfigurationProperties

data class MilvusDataProperties(val database: String, val collection: String)

@ConfigurationProperties(prefix = "milvus")
data class MilvusProperties(val host: String, val port: Int, val data: MilvusDataProperties)
