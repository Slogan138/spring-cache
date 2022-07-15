package io.slogan.cache.webflux

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.slogan.cache.webflux.controller.DataController
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [WebfluxApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
internal class GetTests(
    val dataController: DataController,
) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    init {
        this.describe("Get Value By `hello` then Return `world`") {
            val input = "hello"
            dataController.get(input).block()?.body shouldBe "world"
        }
    }
}