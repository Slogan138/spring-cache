package io.slogan.cache.webflux

import io.slogan.cache.webflux.controller.DataController
import io.slogan.cache.webflux.dao.DataAccess
import io.slogan.cache.webflux.exception.DuplicateKeyException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant

@Suppress("ReactiveStreamsUnusedPublisher")
@SpringBootTest(
    classes = [WebfluxApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class WebfluxApplicationTests {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var dataController: DataController

    @Autowired
    lateinit var dataAccess: DataAccess

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun callGetMethodByHello_thenReturnWorld() {
        val input = "hello"
        val result = dataController.get(input)
        log.debug("Data: {}", result)
        Assertions.assertEquals("world", result.block()!!.body)
    }

    @Test
    fun callGetMethodByTEST_thenReturnNull() {
        val input = "TEST"
        Assertions.assertThrowsExactly(IllegalArgumentException::class.java) {
            log.debug(dataController.get(input).block()!!.body.toString())
        }
    }

    @Test
    fun callGetURLByTest_thenReturnErrorResponse() {
        val input = "TEST"
        webTestClient.get().uri("/api/data").attribute("key", input).accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isBadRequest.expectBody(
                HashMap::class.java
            )
    }

    @Test
    fun callGetMethodByHello_thenReturnCacheValue() {
        // Before Test
        val key = "cached_" + Instant.now().epochSecond.toString()
        val value = "create_pass"
        dataController.create(hashMapOf(key to value))

        val getValue = dataController.get(key)
        dataAccess.deleteData(key)
        dataAccess.insertData(key, "update_pass")

        Assertions.assertEquals(value, getValue.block()!!.body)
    }

    @Test
    fun callCreateMethod_thenReturnSendData() {
        val key = "create_test_" + Instant.now().epochSecond.toString()
        val value = "pass"
        val input = hashMapOf(key to value)
        val result = dataController.create(input)
        Assertions.assertEquals(arrayListOf("$key:$value\n"), result.blockFirst()!!.body)
        Assertions.assertEquals(value, dataController.get(key).block()!!.body)
    }

    @Test
    fun callCreateMethodByHello_thenThrowDuplicateKeyException() {
        val key = "hello"
        val value = "test_input"
        val input = hashMapOf(key to value)
        Assertions.assertThrowsExactly(DuplicateKeyException::class.java) {
            dataController.create(input).blockFirst()!!.body.toString()
        }
    }

    @Test
    fun callUpdateMethodByUpdate_thenReturnWorldCurrentTimestamp() {
        val key = "update"
        val value = "pass" + Instant.now().epochSecond.toString()
        val input = hashMapOf(key to value)
        val result = dataController.update(input)
        Assertions.assertEquals(arrayListOf("$key:$value\n"), result.blockFirst()!!.body)
        Assertions.assertEquals(value, dataController.get(key).block()!!.body)
    }

    @Test
    fun callUpdateMethodByDuplicateKey_theThrowIllegalArgumentException() {
        val key = "duplicate"
        val value = Instant.now().epochSecond.toString()
        Assertions.assertThrowsExactly(IllegalArgumentException::class.java) {
            dataController.update(hashMapOf(key to value))
        }
    }

    @Test
    fun callDeleteMethodByDeleteTest_thenReturnTrue() {
        val key = "delete_test"
        val value = Instant.now().epochSecond.toString()
        dataController.create(hashMapOf(key to value))
        log.debug("Data was input: {}", dataController.get(key).block()!!.body)
        Assertions.assertEquals(ResponseEntity.ok(true), dataController.delete(key).blockOptional().get())
    }

    @Test
    fun callFlushCacheMethod_thenReturnTrue() {
        // Before Test
        val key = "flush_" + Instant.now().epochSecond.toString()
        val value = "create_pass"
        dataController.create(hashMapOf(key to value))

        val updatedValue = "update_pass"
        dataAccess.deleteData(key)
        dataAccess.insertData(key, updatedValue)
        var getValue = dataController.get(key)

        log.debug("value: {}, getValue: {}", value, getValue.block()!!.body)
        Assertions.assertNotEquals(value, getValue.block()!!.body)

        Assertions.assertEquals(ResponseEntity.ok(true), dataController.flushCache("hello").blockOptional().get())

        getValue = dataController.get(key)
        Assertions.assertEquals(updatedValue, getValue.block()!!.body)
    }
}
