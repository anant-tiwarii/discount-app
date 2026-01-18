package io.nexure.discount

import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetProductTest : BaseIntegrationTest() {

    @Test
    fun `GET products returns 200`() {
        createSchema()

        container.execInContainer(
            "psql",
            "-U", container.username,
            "-d", container.databaseName,
            "-c", "INSERT INTO products(id,name,base_price,country) VALUES (gen_random_uuid(),'Test',100,'Sweden');"
        )

        appTest {
            val response = client.get("/api/v1/products?country=Sweden")
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }
}
