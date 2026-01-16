package io.nexure.discount.repository

import io.nexure.discount.model.schema.Product

interface ProductRepository {

    enum class ApplyResult {
        APPLIED,
        ALREADY_EXISTS,
        NOT_FOUND
    }

    suspend fun getByCountry(country: String): List<Product>

    suspend fun applyDiscount(
        productId: String,
        discountId: String,
        percent: Double
    ): ApplyResult
}
