package io.nexure.discount

import org.testcontainers.containers.PostgreSQLContainer

class PostgresTestContainer private constructor() :
    PostgreSQLContainer<PostgresTestContainer>("postgres:15-alpine") {

    companion object {
        val instance: PostgresTestContainer by lazy {
            PostgresTestContainer().apply {
                withDatabaseName("test")
                withUsername("test")
                withPassword("test")
                start()
            }
        }
    }
}