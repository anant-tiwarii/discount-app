package io.nexure.discount.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApplyDiscountRequest(
    val discountId: String,
    val percent: Double
)
