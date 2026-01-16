package io.nexure.discount.route

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

internal fun Route.configureHealthCheck() {

    get("/health"){
        call.respond("SUCCESS")
    }

}