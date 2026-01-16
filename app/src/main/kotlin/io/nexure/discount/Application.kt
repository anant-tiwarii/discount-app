package io.nexure.discount

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import io.nexure.discount.config.Configuration
import io.nexure.discount.config.PostgresConfig
import io.nexure.discount.plugin.configureCache
import io.nexure.discount.plugin.configureDependencyInjection
import io.nexure.discount.plugin.configureRouting
import io.nexure.discount.plugin.configureSerialization

fun main(args: Array<String>): Unit =
    EngineMain.main(args)


@Suppress("unused")
fun Application.module() {
        Configuration.initConfig(environment)
        configureDependencyInjection()
        PostgresConfig.connect()
        configureCache()
        configureSerialization()
        configureRouting()
}
