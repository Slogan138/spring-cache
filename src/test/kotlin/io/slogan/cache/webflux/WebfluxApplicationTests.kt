package io.slogan.cache.webflux

import io.slogan.cache.webflux.controller.DataController
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [WebfluxApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class WebfluxApplicationTests {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var dataController: DataController

    @Test
    fun getCalled_thenShouldReturnNullButPrintData() {
        val result = dataController.get("hello")
        log.debug("Data: {}", result)
        Assertions.assertEquals("world", result.block())
    }
}
