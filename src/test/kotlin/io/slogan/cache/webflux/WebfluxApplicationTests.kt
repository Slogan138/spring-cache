package io.slogan.cache.webflux

import io.slogan.cache.webflux.controller.DataController
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

    /*
    @Test
    fun getCalled_thenShouldReturnNullButPrintData() {
        val mono = dataController.get("key")
        var result: String

        mono.subscribe {
            result = it
            print(it)
        }

        Assertions.assertNull(result)
    }
    */
}
