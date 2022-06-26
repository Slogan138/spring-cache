package io.slogan.cache.webflux

import io.slogan.cache.webflux.controller.DataController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [WebfluxApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class WebfluxApplicationTests {

    @Autowired
    lateinit var dataController: DataController

    /*@Test
    fun getCalled_thenShouldReturnNullButPrintData() {
        val result = dataController.get("key")
        assert(false, { result })
    }*/
}
