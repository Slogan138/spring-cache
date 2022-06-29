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
    fun create(@RequestBody request: Map<String, String>): Mono<String> = Mono.justOrEmpty("")

    @DeleteMapping("/api/data/{key}")
    fun deleteCache(@PathVariable key: String): Mono<Boolean> = Mono.justOrEmpty(dataService.deleteCache(key))
}
