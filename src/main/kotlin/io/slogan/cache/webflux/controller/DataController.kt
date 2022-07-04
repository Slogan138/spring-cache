package io.slogan.cache.webflux.controller

import io.slogan.cache.webflux.service.DataService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class DataController(
    val dataService: DataService
) {

    @GetMapping("/api/data")
    fun get(@RequestParam key: String): Mono<String> = Mono.justOrEmpty(dataService.get(key))

    @PostMapping("/api/data")
    fun create(@RequestBody request: Map<String, String>): Mono<List<String>> {
        val response = arrayListOf<String>()
        request.forEach { (k, v) -> dataService.create(k, v)?.let { response.add(it) } }
        return Mono.justOrEmpty(response)
    }

    @DeleteMapping("/api/data/{key}")
    fun delete(@PathVariable key: String): Mono<Boolean> = Mono.justOrEmpty(dataService.delete(key))

    @DeleteMapping("/api/data/flush/{key}")
    fun deleteCache(@PathVariable key: String): Mono<Boolean> = Mono.justOrEmpty(dataService.flushCache(key))
}
