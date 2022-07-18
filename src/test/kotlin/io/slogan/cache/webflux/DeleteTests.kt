package io.slogan.cache.webflux

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.slogan.cache.webflux.controller.DataController
import io.slogan.cache.webflux.dao.DataAccess
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import java.time.Instant

@SpringBootTest(
    classes = [WebfluxApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
class DeleteTests(
    val dataController: DataController,
    val dataAccess: DataAccess,
) : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    init {
        this.describe("Delete Key By `delete_test` then return true") {
            val key = "delete_test"
            val value = Instant.now().epochSecond.toString()
            dataController.create(mapOf(key to value)).blockFirst()!!.body shouldBe listOf("$key:$value\n")
            dataController.delete(key).blockOptional().get() shouldBe ResponseEntity.ok(true)
        }

        this.describe("Flush Cache By `flush_\$currentTimeStamp` then return true") {
            val key = "flush_" + Instant.now().epochSecond.toString()
            val value = "create_pass"
            dataController.create(mapOf(key to value)).blockFirst()!!.body shouldBe listOf("$key:$value\n")
            dataController.get(key).block()!!.body shouldBe value

            val updateValue = "update_pass"
            dataAccess.deleteData(key) shouldBe true
            dataAccess.insertData(key, updateValue) shouldBe "$key:$updateValue\n"

            var getValue = dataController.get(key).block()!!.body
            getValue shouldBe value

            dataController.flushCache(key).block()!!.body shouldBe true

            dataController.get(key).block()!!.body shouldBe updateValue
        }
    }
}