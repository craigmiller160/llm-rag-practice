package us.craigmiller160.llm.ragpractice.lowlevel.v1.web.dto

data class StoreDocumentResponse(val mongoId: String, val milvusIds: List<Long>)
