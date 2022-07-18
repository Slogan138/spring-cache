package io.slogan.cache.webflux

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.slogan.cache.webflux.controller.DataController
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant

@SpringBootTest(
    classes = [WebfluxApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
class UpdateTests(
    val dataController: DataController
) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    init {
        this.describe("Update Value By `update` then Return concatenation world and current time stamp.") {
            val key = "update"
            val value = "pass_" + Instant.now().epochSecond
            dataController.update(mapOf(key to value)).blockFirst()!!.body shouldBe listOf("$key:$value\n")
            dataController.get(key).block()!!.body shouldBe value
        }

        this.describe("Update Value By `duplicate` then Return throw IllegalArgumentException.") {
            val key = "duplicate"
            val value = Instant.now().epochSecond.toString()
            shouldThrowExactly<IllegalArgumentException> { dataController.update(mapOf(key to value)) }
        }
    }
}