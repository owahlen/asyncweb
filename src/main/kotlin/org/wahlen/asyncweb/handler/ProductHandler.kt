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
import org.wahlen.asyncweb.model.Product
import org.wahlen.asyncweb.service.ProductService

@Component
class ProductHandler(
    private val productService: ProductService
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
        val products: Flow<Product> = productService.getAllProducts()
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyAndAwait(products)
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
                required = true
            )
        ]
    )
    suspend fun getProductById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull()
        return if (id != null) {
            val product = productService.getProductById(id)
            if (product != null) {
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(product)
            } else {
                ServerResponse.notFound().buildAndAwait()
            }
        } else {
            ServerResponse.badRequest().buildAndAwait()
        }
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
            )
        ], requestBody = RequestBody(
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = Product::class)
            )], description = "Product to be created", required = true
        )
    )

    suspend fun createProduct(request: ServerRequest): ServerResponse {
        val product = request.awaitBody<Product>()
        val createdProduct = productService.createProduct(product)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(createdProduct)
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
            )
        ]
    )
    suspend fun updateProduct(
        @Parameter(description = "ID of the product to be updated", required = true)
        request: ServerRequest
    ): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val product = request.awaitBody<Product>().copy(id = id)
        val updatedProduct = productService.updateProduct(product)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(updatedProduct)
    }

    @Operation(
        summary = "Delete a product", description = "Delete a product from the database", responses = [
            ApiResponse(
                responseCode = "204",
                description = "Product deleted"
            )
        ]
    )
    suspend fun deleteProduct(
        @Parameter(description = "ID of the product to be deleted", required = true)
        request: ServerRequest
    ): ServerResponse {
        val id = request.pathVariable("id").toLong()
        productService.deleteProductById(id)
        return ServerResponse.noContent().buildAndAwait()
    }

}