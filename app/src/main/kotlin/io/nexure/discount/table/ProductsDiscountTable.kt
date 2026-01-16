package io.nexure.discount.table

import org.jetbrains.exposed.sql.Table

object ProductDiscountsTable : Table("product_discounts") {
    val productId = uuid("product_id").references(ProductsTable.id)
    val discountId = text("discount_id")
    val percent = decimal("percent", 5, 2)

    override val primaryKey = PrimaryKey(productId, discountId)
}