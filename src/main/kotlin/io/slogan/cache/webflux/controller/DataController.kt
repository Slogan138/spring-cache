package io.slogan.cache.webflux.controller

import io.slogan.cache.webflux.service.DataService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class DataController(
    val dataService: DataService
) {

    @GetMapping("/api/data")
    fun get(@RequestParam key: String): Mono<ResponseEntity<String>> =
        Mono.justOrEmpty(ResponseEntity.ok(dataService.get(key)))

    @PostMapping("/api/data")
    fun create(@RequestBody request: Map<String, String>): Flux<ResponseEntity<List<String>>> {
        val response = arrayListOf<String>()
        request.forEach { (k, v) -> dataService.create(k, v).let { response.add(it) } }
        return Flux.just(ResponseEntity.ok(response))
    }

    @PutMapping("/api/data")
    fun update(@RequestBody request: Map<String, String>): Flux<ResponseEntity<List<String>>> {
        val response = arrayListOf<String>()
        request.forEach { (k, v) -> dataService.update(k, v).let { response.add(it) } }
        return Flux.just(ResponseEntity.ok(response))
    }

    @DeleteMapping("/api/data/{key}")
    fun delete(@PathVariable key: String): Mono<ResponseEntity<Boolean>> =
        Mono.justOrEmpty(ResponseEntity.ok(dataService.delete(key)))

    @DeleteMapping("/api/data/flush/{key}")
    fun flushCache(@PathVariable key: String): Mono<ResponseEntity<Boolean>> =
        Mono.justOrEmpty(ResponseEntity.ok(dataService.flushCache(key)))
}
