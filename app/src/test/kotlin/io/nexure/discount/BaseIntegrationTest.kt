package io.nexure.discount

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.nexure.discount.config.Configuration
import io.nexure.discount.config.PostgresConfig
import io.nexure.discount.plugin.*
import io.nexure.discount.table.CountryVatTable
import io.nexure.discount.table.ProductDiscountsTable
import io.nexure.discount.table.ProductsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

abstract class BaseIntegrationTest {

    protected val container = PostgresTestContainer.instance

    fun createSchema() {
        Database.connect(
            url = container.jdbcUrl,
            user = container.username,
            password = container.password
        )

        transaction {
            SchemaUtils.create(CountryVatTable, ProductsTable, ProductDiscountsTable)
        }
    }

    fun appTest(block: suspend ApplicationTestBuilder.() -> Unit) =
        testApplication {
            environment {
                config = MapApplicationConfig(
                    "ktor.postgres.psgUrl" to container.jdbcUrl,
                    "ktor.postgres.psgUsername" to container.username,
                    "ktor.postgres.psgPassword" to container.password,
                    "ktor.postgres.hikariPoolSize" to "5",
                )
            }

            application {
                Configuration.initConfig(environment)
                configureDependencyInjection()
                PostgresConfig.connect()
                configureCache()
                configureSerialization()
                configureRouting()
            }


            block()
        }
}