package io.nexure.discount.model

data class ConfigParameters(
    val psgUrl: String,
    val psgUsername: String,
    val psgPassword: String,
    val hikariPoolSize: Int

)
