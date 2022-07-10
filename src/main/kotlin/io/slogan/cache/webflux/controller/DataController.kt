package io.slogan.cache.webflux.controller

import io.slogan.cache.webflux.service.DataService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@RestController
class DataController(
    val dataService: DataService
) {

    @GetMapping("/api/data")
    fun get(@RequestParam key: String): Mono<ServerResponse> = buildResponse(dataService.get(key))

    // TODO: Change Mono to Flux
    @PostMapping("/api/data")
    fun create(@RequestBody request: Map<String, String>): Mono<ServerResponse> {
        val response = arrayListOf<String>()
        request.forEach { (k, v) -> dataService.create(k, v).let { response.add(it) } }
        return buildResponse(response)
    }

    // TODO: Change Mono to Flux
    @PutMapping("/api/data")
    fun update(@RequestBody request: Map<String, String>): Mono<ServerResponse> {
        val response = arrayListOf<String>()
        request.forEach { (k, v) -> dataService.update(k, v).let { response.add(it) } }
        return buildResponse(response)
    }

    @DeleteMapping("/api/data/{key}")
    fun delete(@PathVariable key: String): Mono<ServerResponse> = buildResponse(dataService.delete(key))

    @DeleteMapping("/api/data/flush/{key}")
    fun deleteCache(@PathVariable key: String): Mono<ServerResponse> = buildResponse(dataService.flushCache(key))

    private fun buildResponse(data: Any): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(BodyInserters.fromValue(data))
    }
}
