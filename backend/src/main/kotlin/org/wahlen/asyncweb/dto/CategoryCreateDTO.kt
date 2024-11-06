package org.wahlen.asyncweb.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.wahlen.asyncweb.model.Category

data class CategoryCreateDTO (
    @Schema(description = "The name of the category", example = "books")
    @field:NotBlank
    @field:Size(max=255)
    val name: String?
)

fun CategoryCreateDTO.toCategory() : Category =
    Category(name = name!!)
