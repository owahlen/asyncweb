package org.wahlen.asyncweb.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.wahlen.asyncweb.model.Category
import org.wahlen.asyncweb.model.Product

data class ProductResponseDTO(
    @Schema(description = "The id of the product", example = "1")
    val id: Long,

    @Schema(description = "The id of the category", example = "1")
    val categoryId: Long,

    @Schema(description = "The name of the category", example = "books")
    val categoryName: String,

    @Schema(description = "The name of the product", example = "The Lord of the Rings")
    val name: String,

    @Schema(description = "The price of the product", example = "19.99")
    val price: Double
)

fun Product.toProductResponseDTO(category: Category): ProductResponseDTO =
    ProductResponseDTO(id = id!!, categoryId = category.id!!, categoryName = category.name, name = name, price = price)
