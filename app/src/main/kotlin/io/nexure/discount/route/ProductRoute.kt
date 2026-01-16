package io.nexure.discount.route


import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.nexure.discount.model.dto.ApplyDiscountRequest
import io.nexure.discount.service.ProductService

fun Route.configureProductRoute(productService: ProductService) {


    route("/products") {

        /**
         * GET /products?country=Sweden
         */
        get {
            val country = call.request.queryParameters["country"]
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "country query parameter is required")
                )

            val products = productService.getProductsByCountry(country)

            call.respond(products)
        }

        /**
         * PUT /products/{id}/discount
         */
        put("/{id}/discount") {
            val productId = call.parameters["id"]
                ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "product id path parameter required")
                )

            val request = runCatching {
                call.receive<ApplyDiscountRequest>()
            }.getOrElse {
                return@put call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "invalid request body")
                )
            }

            val result = productService.applyDiscount(
                productId = productId,
                discountId = request.discountId,
                percent = request.percent
            )

            when (result) {
                ProductService.DiscountResult.APPLIED ->
                    call.respond(HttpStatusCode.OK, mapOf("message" to "discount applied"))
                ProductService.DiscountResult.ALREADY_EXISTS ->
                    call.respond(HttpStatusCode.OK, mapOf("message" to "discount already applied"))
                ProductService.DiscountResult.NOT_FOUND ->
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf("error" to "product not found")
                    )
                ProductService.DiscountResult.INVALID ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "invalid discount percent or discount id")
                    )
            }
        }
    }
}
