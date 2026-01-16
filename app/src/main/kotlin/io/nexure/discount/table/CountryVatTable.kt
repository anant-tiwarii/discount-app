package io.nexure.discount.table

import org.jetbrains.exposed.sql.Table

object CountryVatTable : Table("country_vat") {
    val country = text("country").uniqueIndex()
    val vatPercent = decimal("vat_percent",5,2)
}
