package org.wahlen.asyncweb.handler

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Validator
import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.wahlen.asyncweb.config.checkValid
import org.wahlen.asyncweb.dto.ProductCreateDTO
import org.wahlen.asyncweb.dto.ProductResponseDTO
import org.wahlen.asyncweb.dto.ProductUpdateDTO
import org.wahlen.asyncweb.model.Product
import org.wahlen.asyncweb.service.ProductService

@Component
class ProductHandler(
    private val productService: ProductService,
    private val validator: Validator
) {

    @Operation(
        summary = "Get all products", description = "Retrieve all products in the database", responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of products",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = ArraySchema(
                        schema = Schema(implementation = Product::class)
                    )
                )]
            )
        ]
    )
    suspend fun getAllProducts(request: ServerRequest): ServerResponse {
        val productResponseDTOs: Flow<ProductResponseDTO> = productService.getAllProductResponseDTOs()
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyAndAwait(productResponseDTOs)
    }

    @Operation(
        summary = "Get product by ID", description = "Retrieve a product by its ID", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Product found",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Product::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Product not found"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid ID"
            )
        ], parameters = [
            Parameter(
                `in` = ParameterIn.PATH,
                name = "id",
                description = "ID of the product to be retrieved",
                required = true,
                schema = Schema(type = "integer", format = "int64")
            )
        ]
    )
    suspend fun getProductById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
        val productResponseDTO = productService.getProductResponseDTOById(id) // throws NoSuchElementException if not found
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(productResponseDTO)
    }

    @Operation(
        summary = "Create a new product", description = "Create a new product in the database", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Product created",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Product::class)
                )]
            ), ApiResponse(
                responseCode = "400",
                description = "Invalid product"
            ), ApiResponse(
                responseCode = "422",
                description = "Category not found"
            )
        ], requestBody = RequestBody(
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ProductCreateDTO::class)
            )], description = "Product to be created", required = true
        )
    )
    suspend fun createProduct(request: ServerRequest): ServerResponse {
        val productCreateDTO = request.awaitBody<ProductCreateDTO>()
        validator.checkValid(productCreateDTO)
        val createdProductResponseDTO = productService.createProduct(productCreateDTO)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(createdProductResponseDTO)
    }

    @Operation(
        summary = "Update a product", description = "Update an existing product in the database", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Product updated",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Product::class)
                )]
            ), ApiResponse(
                responseCode = "400",
                description = "Invalid product"
            ), ApiResponse(
                responseCode = "404",
                description = "Product not found"
            ), ApiResponse(
                responseCode = "422",
                description = "Category not found"
            )
        ], parameters = [
            Parameter(
                `in` = ParameterIn.PATH,
                name = "id",
                description = "ID of the product to be updated",
                required = true,
                schema = Schema(type = "integer", format = "int64")
            )
        ], requestBody = RequestBody(
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ProductUpdateDTO::class)
            )], description = "Product to be updated", required = true
        )
    )
    suspend fun updateProduct(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
        val productUpdateDTO = request.awaitBody<ProductUpdateDTO>()
        validator.checkValid(productUpdateDTO)
        val updatedProductResponseDTO = productService.updateProduct(id, productUpdateDTO)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(updatedProductResponseDTO)
    }

    @Operation(
        summary = "Delete a product", description = "Delete a product from the database", responses = [
            ApiResponse(
                responseCode = "204",
                description = "Product deleted"
            ), ApiResponse(
                responseCode = "404",
                description = "Product not found"
            )
        ], parameters = [
            Parameter(
                `in` = ParameterIn.PATH,
                name = "id",
                description = "ID of the product to be deleted",
                required = true,
                schema = Schema(type = "integer", format = "int64")
            )
        ]
    )
    suspend fun deleteProduct(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
        productService.deleteProductById(id)
        return ServerResponse.noContent().buildAndAwait()
    }
}