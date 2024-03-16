package us.craigmiller160.llm.ragpractice.lowlevel.web.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import us.craigmiller160.llm.ragpractice.lowlevel.web.dto.SearchRequest
import us.craigmiller160.llm.ragpractice.lowlevel.web.dto.StoreDocumentRequest
import us.craigmiller160.llm.ragpractice.lowlevel.web.dto.StoreDocumentResponse
import us.craigmiller160.llm.ragpractice.lowlevel.web.service.VectorService

@RestController
@RequestMapping("/vector")
class VectorController(private val vectorService: VectorService) {
  @PostMapping("/document")
  fun storeDocument(@RequestBody request: StoreDocumentRequest): StoreDocumentResponse =
      vectorService.storeDocument(request)

  @PostMapping("/search")
  fun search(@RequestBody request: SearchRequest) = vectorService.search(request)
}
