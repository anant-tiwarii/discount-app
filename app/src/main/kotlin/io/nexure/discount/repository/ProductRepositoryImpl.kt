package io.nexure.discount.repository

import io.nexure.discount.config.dbQuery
import io.nexure.discount.model.schema.Product
import io.nexure.discount.model.schema.Discount
import io.nexure.discount.table.ProductDiscountsTable
import io.nexure.discount.table.ProductsTable
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID


class ProductRepositoryImpl : ProductRepository {

    override suspend fun getByCountry(country: String): List<Product> = dbQuery {

        val products = ProductsTable
            .selectAll().where { ProductsTable.country.lowerCase() eq country.lowercase() }
            .map {
                Product(
                    id = it[ProductsTable.id].toString(),
                    name = it[ProductsTable.name],
                    basePrice = it[ProductsTable.basePrice].toDouble(),
                    country = it[ProductsTable.country],
                    discounts = emptyList()
                )
            }

        if (products.isEmpty()) return@dbQuery emptyList()

        val productIds = products.map { UUID.fromString(it.id) }

        val discountRows = ProductDiscountsTable
            .selectAll().where { ProductDiscountsTable.productId inList productIds }
            .map {
                it[ProductDiscountsTable.productId] to
                        Discount(
                            it[ProductDiscountsTable.discountId],
                            it[ProductDiscountsTable.percent].toDouble()
                        )
            }

        // attach discounts to products
        val discountMap = discountRows.groupBy({ it.first }, { it.second })

        products.map { p ->
            p.copy(discounts = discountMap[UUID.fromString(p.id)] ?: emptyList())
        }
    }


    override suspend fun applyDiscount(
        productId: String,
        discountId: String,
        percent: Double
    ): ProductRepository.ApplyResult = dbQuery {

        val pid = UUID.fromString(productId)

        val exists = ProductsTable
            .selectAll().where { ProductsTable.id eq pid }
            .empty()
            .not()

        if (!exists) {
            return@dbQuery ProductRepository.ApplyResult.NOT_FOUND
        }

        val insertResult = ProductDiscountsTable.insertIgnore {
            it[ProductDiscountsTable.productId] = pid
            it[ProductDiscountsTable.discountId] = discountId
            it[ProductDiscountsTable.percent] = percent.toBigDecimal()
        }

        return@dbQuery if (insertResult.insertedCount == 0)
            ProductRepository.ApplyResult.ALREADY_EXISTS
        else
            ProductRepository.ApplyResult.APPLIED
    }


}
