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
        Assertions.assertEquals("world", result.body?.block())
    }

    @Test
    fun callGetMethodByTEST_thenReturnNull() {
        val input = "TEST"
        Assertions.assertThrowsExactly(IllegalArgumentException::class.java) {
            log.debug(dataController.get(input).body?.block().toString())
        }
    }

    @Test
    fun callCreateMethod_thenReturnSendData() {
        val key = "create_test" + Instant.now().epochSecond.toString()
        val value = "pass"
        val input = hashMapOf(key to value)
        val result = dataController.create(input)
        Assertions.assertEquals(arrayListOf("$key:$value\n"), result.body?.blockFirst())
        Assertions.assertEquals(value, dataController.get(key).body?.block())
    }

    @Test
    fun callCreateMethodByHello_thenThrowException() {
        val key = "hello"
        val value = "test_input"
        val input = hashMapOf(key to value)
        Assertions.assertThrowsExactly(DuplicateKeyException::class.java) {
            log.debug(dataController.create(input).body?.blockFirst().toString())
        }
    }

    @Test
    fun callUpdateMethodByUpdate_thenReturnWorldCurrentTimestamp() {
        val key = "update"
        val value = "pass" + Instant.now().epochSecond.toString()
        val input = hashMapOf(key to value)
        val result = dataController.update(input)
        Assertions.assertEquals(arrayListOf("$key:$value\n"), result.body?.blockFirst())
        Assertions.assertEquals(value, dataController.get(key).body?.block())
    }

    // TODO: Cache Flush 여부 검증 가능하도록 수정
    @Test
    fun callFlushCacheMethod_thenReturnTrue() {
        Assertions.assertTrue { dataController.deleteCache("hello").body!!.blockOptional().get() }
    }
}
