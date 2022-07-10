package io.slogan.cache.webflux.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.io.IOException

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun illegalExceptionHandler(exception: IllegalArgumentException): Mono<ServerResponse> {
        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(
            BodyInserters.fromValue(
                mapOf("message" to exception.message, "code" to 400)
            )
        )
    }

    @ExceptionHandler(value = [IOException::class])
    fun ioExceptionHandler(exception: IOException): Mono<ServerResponse> {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                BodyInserters.fromValue(mapOf("message" to exception.message, "code" to 500))
            )
    }
}