package io.nexure.discount.repository

import io.nexure.discount.table.CountryVatTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class VatRepositoryImpl : VatRepository {
    override suspend fun loadAll(): List<Pair<String, BigDecimal>> =
        transaction {
            CountryVatTable.selectAll().map {
                it[CountryVatTable.country] to it[CountryVatTable.vatPercent]
            }
        }
}