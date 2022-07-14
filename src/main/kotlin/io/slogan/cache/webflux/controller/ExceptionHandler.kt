package io.slogan.cache.webflux.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono
import java.io.IOException

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun illegalExceptionHandler(exception: IllegalArgumentException): ResponseEntity<Mono<Map<String, Any>>> {
        return ResponseEntity.badRequest()
            .body(Mono.justOrEmpty(hashMapOf<String, Any>("message" to "Illegal Input", "code" to 400)))
    }

    @ExceptionHandler(value = [IOException::class])
    fun ioExceptionHandler(exception: IOException): ResponseEntity<Mono<Map<String, Any>>> {
        return ResponseEntity.internalServerError()
            .body(Mono.justOrEmpty(hashMapOf<String, Any>("message" to "Internal Server Error", "code" to 500)))
    }
}