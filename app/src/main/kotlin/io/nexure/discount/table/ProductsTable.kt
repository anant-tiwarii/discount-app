package io.nexure.discount.table

import org.jetbrains.exposed.sql.Table

object ProductsTable : Table("products") {
    val id = uuid("id")
    val name = text("name")
    val basePrice = decimal("base_price", 10, 2)
    val country = text("country")

    override val primaryKey = PrimaryKey(id)
}
