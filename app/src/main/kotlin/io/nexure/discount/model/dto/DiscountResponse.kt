package io.nexure.discount.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class DiscountResponse(
    val discountId: String,
    val percent: Double
)
