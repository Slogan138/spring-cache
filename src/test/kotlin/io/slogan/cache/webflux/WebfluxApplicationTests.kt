package io.slogan.cache.webflux

import io.slogan.cache.webflux.controller.DataController
import io.slogan.cache.webflux.exception.DuplicateKeyException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant

@SpringBootTest(
    classes = [WebfluxApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class WebfluxApplicationTests {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var dataController: DataController

    @Test
    fun callGetMethodByHello_thenReturnWorld() {
        val input = "hello"
        val result = dataController.get(input)
        log.debug("Data: {}", result)
        Assertions.assertEquals("world", result.block())
    }

    @Test
    fun callGetMethodByTEST_thenReturnNull() {
        val input = "TEST"
        val result = dataController.get(input)
        Assertions.assertNull(result.block())
    }

    @Test
    fun callCreateMethod_thenReturnSendData() {
        val key = "create_test" + Instant.now().epochSecond.toString()
        val value = "pass"
        val input = hashMapOf(key to value)
        val result = dataController.create(input)
        Assertions.assertEquals(arrayListOf("$key:$value\n"), result.block())
        Assertions.assertEquals(value, dataController.get(key).block())
    }

    @Test
    fun callCreateMethodByHello_thenThrowException() {
        val key = "hello"
        val value = "test input"
        val input = hashMapOf(key to value)
        Assertions.assertThrowsExactly(DuplicateKeyException::class.java) {
            log.debug(
                dataController.create(input).block().toString()
            )
        }
    }
}
