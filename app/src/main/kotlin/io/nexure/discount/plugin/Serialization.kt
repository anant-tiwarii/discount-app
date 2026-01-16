package io.nexure.discount.plugin

import com.fasterxml.jackson.databind.util.StdDateFormat
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import org.slf4j.event.Level


fun Application.configureSerialization() {

    install(ContentNegotiation) {
        jackson {
            dateFormat = StdDateFormat()
        }
    }

    install(CallLogging) {
        level = Level.INFO

        filter { call ->
            call.request.path().startsWith("/")
        }

        format { call ->
            "${call.request.httpMethod.value} ${call.request.path()} ${call.response.status()?.value}"
        }
    }

}