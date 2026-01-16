package io.nexure.discount.model.cache

import java.math.BigDecimal

object VatCache {
    private val vatByCountry = mutableMapOf<String, BigDecimal>()

    fun get(country: String): BigDecimal? =
        vatByCountry[country.lowercase()]

    fun put(country: String, vat: BigDecimal) {
        vatByCountry[country.lowercase()] = vat
    }

    fun putAll(vats: List<Pair<String, BigDecimal>>) {
        vats.forEach { (country, vat) -> put(country, vat) }
    }
}