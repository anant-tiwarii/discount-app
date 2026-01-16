package io.nexure.discount.service

import io.nexure.discount.model.cache.VatCache
import io.nexure.discount.model.dto.DiscountResponse
import io.nexure.discount.model.dto.ProductResponse
import io.nexure.discount.repository.ProductRepository


class ProductService(
    private val productRepository: ProductRepository
) {

    enum class DiscountResult {
        APPLIED,
        ALREADY_EXISTS,
        NOT_FOUND,
        INVALID
    }

    suspend fun getProductsByCountry(country: String): List<ProductResponse> {

        val vatPercent = VatCache.get(country.lowercase())
            ?: return emptyList() // or throw, depending on desired behavior

        val products = productRepository.getByCountry(country)

        return products.map { product ->

            val totalDiscount = product.discounts.sumOf { it.percent } / 100.0

            val finalPrice = product.basePrice *
                    (1 - totalDiscount) *
                    (1 + vatPercent.toDouble()/ 100.0)

            ProductResponse(
                id = product.id,
                name = product.name,
                basePrice = product.basePrice,
                country = product.country,
                discounts = product.discounts.map { DiscountResponse(it.discountId, it.percent) },
                finalPrice = finalPrice
            )
        }
    }


    suspend fun applyDiscount(productId: String, discountId: String, percent: Double): DiscountResult {
        if (percent <= 0 || percent >= 100 || discountId == "")
            return DiscountResult.INVALID

        val result = productRepository.applyDiscount(productId, discountId, percent)

        return when (result) {
            ProductRepository.ApplyResult.APPLIED -> DiscountResult.APPLIED
            ProductRepository.ApplyResult.ALREADY_EXISTS -> DiscountResult.ALREADY_EXISTS
            ProductRepository.ApplyResult.NOT_FOUND -> DiscountResult.NOT_FOUND
        }
    }
}
