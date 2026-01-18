package io.nexure.discount

import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProductDiscountTest : BaseIntegrationTest() {

    @Test
    fun `apply discount returns 200`() {
        createSchema()

        val id = "11111111-1111-1111-1111-111111111111"

        container.execInContainer(
            "psql",
            "-U", container.username,
            "-d", container.databaseName,
            "-c", """
                INSERT INTO products(id,name,base_price,country)
                VALUES ('$id','Test',100,'Sweden');
            """.trimIndent()
        )

        appTest {
            val response = client.put("/api/v1/products/$id/discount") {
                contentType(ContentType.Application.Json)
                setBody("""{"discountId":"DISC10","percent":10.0}""")
            }
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }
}
