package io.slogan.cache.webflux

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.slogan.cache.webflux.controller.DataController
import io.slogan.cache.webflux.dao.DataAccess
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant

@SpringBootTest(
    classes = [WebfluxApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
internal class GetTests(
    val dataController: DataController,
    val dataAccess: DataAccess,
    val webTestClient: WebTestClient
) : DescribeSpec() {
    // To Test Spring Application
    override fun extensions() = listOf(SpringExtension)

    init {
        this.describe("Get Value By `hello` then Return `world`") {
            val input = "hello"
            dataController.get(input).block()?.body shouldBe "world"
        }

        this.describe("Get Value By `TEST` then Throw Exception") {
            val input = "TEST"
            shouldThrowExactly<IllegalArgumentException> { dataController.get(input) }
        }

        this.describe("Get Value By `TEST` called url then Throw Error Response") {
            val input = "TEST"
            webTestClient.get().uri("/api/data").attribute("key", input).accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isBadRequest.expectBody(
                    HashMap::class.java
                )
        }

        this.describe("Get Value By `cached` then Return Cache value") {
            val key = "cached_" + Instant.now().epochSecond.toString()
            val value = "create_pass"
            dataController.create(hashMapOf(key to value)).blockFirst()!!.body!![0] shouldBe "$key:$value\n"

            val getValue = dataController.get(key).block()!!.body
            getValue shouldBe value
            dataAccess.deleteData(key) shouldBe true

            val updateValue = "update_pass"
            dataAccess.insertData(key, updateValue) shouldBe "$key:$updateValue\n"

            dataController.get(key).block()!!.body shouldBe getValue
        }
    }
}