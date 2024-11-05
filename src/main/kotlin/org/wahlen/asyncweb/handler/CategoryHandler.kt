package org.wahlen.asyncweb.handler

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.wahlen.asyncweb.dto.CategoryCreateDTO
import org.wahlen.asyncweb.dto.CategoryUpdateDTO
import org.wahlen.asyncweb.model.Category
import org.wahlen.asyncweb.service.CategoryService

@Component
class CategoryHandler(
    private val categoryService: CategoryService
) {

    @Operation(
        summary = "Get all categories", description = "Retrieve all categories in the database", responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of categories",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = ArraySchema(
                        schema = Schema(implementation = Category::class)
                    )
                )]
            )
        ]
    )
    suspend fun getAllCategories(request: ServerRequest): ServerResponse {
        val categories: Flow<Category> = categoryService.getAllCategories()
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyAndAwait(categories)
    }

    @Operation(
        summary = "Get category by ID", description = "Retrieve a category by its ID", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Category found",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Category::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Category not found"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid ID"
            )
        ], parameters = [
            Parameter(
                `in` = ParameterIn.PATH,
                name = "id",
                description = "ID of the category to be retrieved",
                required = true,
                schema = Schema(type = "integer", format = "int64")
            )
        ]
    )
    suspend fun getCategoryById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
        val category = categoryService.getCategoryById(id) // throws NoSuchElementException if not found
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(category)
    }

    @Operation(
        summary = "Create a new category", description = "Create a new category in the database", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Category created",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Category::class)
                )]
            )
        ], requestBody = RequestBody(
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = CategoryCreateDTO::class)
            )], description = "Category to be created", required = true
        )
    )
    suspend fun createCategory(request: ServerRequest): ServerResponse {
        val categoryCreateDTO = request.awaitBody<CategoryCreateDTO>()
        val createdCategory = categoryService.createCategory(categoryCreateDTO)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(createdCategory)
    }

    @Operation(
        summary = "Update a category", description = "Update an existing category in the database", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Category updated",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Category::class)
                )]
            )
        ], parameters = [
            Parameter(
                `in` = ParameterIn.PATH,
                name = "id",
                description = "ID of the category to be updated",
                required = true,
                schema = Schema(type = "integer", format = "int64")
            )
        ], requestBody = RequestBody(
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = CategoryUpdateDTO::class)
            )], description = "Category to be updated", required = true
        )
    )
    suspend fun updateCategory(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
        val categoryUpdateDTO = request.awaitBody<CategoryUpdateDTO>()
        val updatedCategory = categoryService.updateCategory(id, categoryUpdateDTO)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(updatedCategory)
    }

    @Operation(
        summary = "Delete a category", description = "Delete a category from the database", responses = [
            ApiResponse(
                responseCode = "204",
                description = "Category deleted"
            )
        ], parameters = [
            Parameter(
                `in` = ParameterIn.PATH,
                name = "id",
                description = "ID of the category to be deleted",
                required = true,
                schema = Schema(type = "integer", format = "int64")
            )
        ]
    )
    suspend fun deleteCategory(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
        categoryService.deleteCategoryById(id)
        return ServerResponse.noContent().buildAndAwait()
    }
}