package io.nexure.discount.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.jetbrains.exposed.sql.Database

object PostgresConfig {

    private val log = LoggerFactory.getLogger(PostgresConfig::class.java)

    fun connect() {
        try {
            val env = Configuration.env

            val hikariConfig = HikariConfig().apply {
                jdbcUrl = env.psgUrl
                username = env.psgUsername
                password = env.psgPassword
                maximumPoolSize = env.hikariPoolSize

                driverClassName = "org.postgresql.Driver"
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_READ_COMMITTED"

                validate()
            }

            val dataSource = HikariDataSource(hikariConfig)

            Database.connect(dataSource)

            log.info("Connected to Postgres successfully!")

        } catch (ex: Exception) {
            log.error("Failed to connect to Postgres :(", ex)
            throw IllegalStateException("Database connection failed", ex)
        }
    }
}