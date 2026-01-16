package io.nexure.discount.repository

import java.math.BigDecimal

interface VatRepository {
    suspend fun loadAll(): List<Pair<String, BigDecimal>>
}