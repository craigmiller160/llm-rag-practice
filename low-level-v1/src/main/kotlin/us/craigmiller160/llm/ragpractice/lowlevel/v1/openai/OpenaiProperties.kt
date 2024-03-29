package us.craigmiller160.llm.ragpractice.lowlevel.v1.openai

import org.springframework.boot.context.properties.ConfigurationProperties
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.ChatModel
import us.craigmiller160.llm.ragpractice.common.spring.openai.dto.dto.common.EmbeddingModel

data class OpenaiParsedModels(val chat: ChatModel, val embedding: EmbeddingModel)

data class OpenaiRawModels(val chat: String, val embedding: String) {
  val parsed: OpenaiParsedModels
    get() =
        OpenaiParsedModels(
            chat = ChatModel.parse(chat), embedding = EmbeddingModel.parse(embedding))
}

@ConfigurationProperties(prefix = "openai")
data class OpenaiProperties(val url: String, val key: String, val models: OpenaiRawModels)
