package io.slogan.cache.webflux.controller

import io.slogan.cache.webflux.service.DataService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class DataController(
    val dataService: DataService
) {

    @GetMapping("/api/data")
    fun get(@RequestParam key: String): Mono<String> = Mono.create { dataService.get(key) }

    @PostMapping("/api/data")
    fun create(@RequestBody request: Map<String, Any>): Mono<String>? = Mono.create { dataService.create(request) }

    @PutMapping("/api/data")
    fun update(@RequestBody request: Map<String, Any>): Mono<String>? = Mono.create { dataService.update(request) }

    @DeleteMapping("/api/data/{key}")
    fun delete(@PathVariable key: String): Mono<Boolean>? = Mono.create { dataService.delete(key) }
}
