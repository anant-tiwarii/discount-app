package io.nexure.discount.config

import io.ktor.server.application.ApplicationEnvironment
import io.nexure.discount.model.ConfigParameters

object Configuration {

    lateinit var env: ConfigParameters

    fun initConfig(environment: ApplicationEnvironment) {
        val postgresConfig = environment.config.config("ktor.postgres")

        env = ConfigParameters(
            psgUrl = postgresConfig.property("psgUrl").getString(),
            psgUsername = postgresConfig.property("psgUsername").getString(),
            psgPassword = postgresConfig.property("psgPassword").getString(),
            hikariPoolSize = postgresConfig.property("hikariPoolSize").getString().toInt()
        )
    }

}