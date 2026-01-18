package io.nexure.discount

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.* // Important
import io.ktor.serialization.jackson.* // Important
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProductDiscountConcurrencyTest : BaseIntegrationTest() {

    @Test
    fun `concurrent discount application results in already applied`() = appTest {
        createSchema()

        val jsonClient = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }

        val id = "22222222-2222-2222-2222-222222222222"
        val discountId = "DISC10"

        container.execInContainer(
            "psql", "-U", container.username, "-d", container.databaseName,
            "-c", "INSERT INTO products(id,name,base_price,country) VALUES ('$id','Test',100,'Sweden');"
        )

        runBlocking {
            val calls = (1..10).map {
                async {
                    jsonClient.put("/api/v1/products/$id/discount") {
                        contentType(ContentType.Application.Json)
                        setBody("""{"discountId":"$discountId","percent":10.0}""")
                    }
                }
            }.awaitAll()

            calls.forEach { response ->
                assertEquals(HttpStatusCode.OK, response.status)
            }

            val bodies = calls.map { it.body<Map<String, String>>() }

            val appliedCount = bodies.count { it["message"] == "discount applied" }
            val alreadyAppliedCount = bodies.count { it["message"] == "discount already applied" }

            assertEquals(1, appliedCount)
            assertEquals(9, alreadyAppliedCount)
        }
    }
}