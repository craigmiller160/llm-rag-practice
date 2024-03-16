package us.craigmiller160.llm.ragpractice.lowlevel.web.dto

data class StoreDocumentResponse(val mongoId: String, val milvusIds: List<Long>)
