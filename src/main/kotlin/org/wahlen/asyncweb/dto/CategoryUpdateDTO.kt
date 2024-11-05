package org.wahlen.asyncweb.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.wahlen.asyncweb.model.Category

data class CategoryUpdateDTO (
    @Schema(description = "The name of the category", example = "books")
    @field:NotBlank
    @field:Size(max=255)
    val name: String
)

fun CategoryUpdateDTO.toCategory(category: Category) : Category =
    category.copy(name = name)
