package io.slogan.cache.webflux

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.slogan.cache.webflux.controller.DataController
import io.slogan.cache.webflux.dao.DataAccess
import io.slogan.cache.webflux.exception.DuplicateKeyException
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant

@SpringBootTest(
    classes = [WebfluxApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
internal class CreateTests(
    val dataController: DataController,
    val dataAccess: DataAccess,
    val webTestClient: WebTestClient
) : DescribeSpec() {
    // To Test Spring Application
    override fun extensions() = listOf(SpringExtension)

    init {
        this.describe("Create Value By `create_test_` then Return key and value") {
            val key = "create_test_" + Instant.now().epochSecond.toString()
            val value = "pass"
            dataController.create(mapOf(key to value)).blockFirst()!!.body shouldBe listOf("$key:$value\n")
        }

        this.describe("Create Value By `Hello` then Throw DuplicateKeyException") {
            val key = "hello"
            val value = "test_input"
            shouldThrowExactly<DuplicateKeyException> { dataController.create(mapOf(key to value)) }
        }
    }
}