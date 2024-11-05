package org.wahlen.asyncweb.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.wahlen.asyncweb.model.Product

data class ProductUpdateDTO(
    @Schema(description = "The id of the product category", example = "1")
    @field:NotNull(message = "categoryId must not be null")
    val categoryId: Long?,

    @Schema(description = "The name of the product", example = "The Lord of the Rings")
    @field:NotBlank(message = "name must not be blank")
    val name: String?,

    @Schema(description = "The price of the product", example = "19.99")
    @field:NotNull(message = "price must not be null")
    @field:Positive(message = "price must be positive")
    val price: Double?
)

fun ProductUpdateDTO.toProduct() : Product =
    Product(categoryId = categoryId!!, name = name!!, price = price!!)
