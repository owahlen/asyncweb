package org.wahlen.asyncweb.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.wahlen.asyncweb.model.Category

data class CategoryResponseDTO (
    @Schema(description = "The id of the category", example = "1")
    val id: Long,

    @Schema(description = "The name of the category", example = "books")
    val name: String
)

fun Category.toCategoryResponseDTO() : CategoryResponseDTO =
    CategoryResponseDTO(id=id!!, name = name)
