package io.nexure.discount.plugin

import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.nexure.discount.route.configureHealthCheck
import io.nexure.discount.route.configureProductRoute
import io.nexure.discount.service.ProductService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureRouting() {

    val productService by closestDI().instance<ProductService>()

    routing {
        route("/api/v1"){
            configureProductRoute(productService)
        }
        configureHealthCheck()
    }

}