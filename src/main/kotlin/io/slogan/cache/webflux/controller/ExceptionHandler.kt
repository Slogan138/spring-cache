package io.slogan.cache.webflux.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono
import java.io.IOException

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun illegalExceptionHandler(exception: IllegalArgumentException): Mono<ResponseEntity<Map<String, Any>>> {
        return Mono.justOrEmpty(
            ResponseEntity.badRequest().body(hashMapOf<String, Any>("message" to "Illegal Input", "code" to 400))
        )
    }

    @ExceptionHandler(value = [IOException::class])
    fun ioExceptionHandler(exception: IOException): Mono<ResponseEntity<Map<String, Any>>> {
        return Mono.justOrEmpty(
            ResponseEntity.internalServerError()
                .body(hashMapOf<String, Any>("message" to "Internal Server Error", "code" to 500))
        )
    }
}