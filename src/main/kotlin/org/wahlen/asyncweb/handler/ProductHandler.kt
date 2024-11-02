package org.wahlen.asyncweb.handler

import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.wahlen.asyncweb.model.Product
import org.wahlen.asyncweb.repository.ProductRepository

@Component
class ProductHandler(
    private val productRepository: ProductRepository
) {

    suspend fun getAllProducts(request: ServerRequest): ServerResponse {
        val products: Flow<Product> = productRepository.findAll()
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyAndAwait(products)
    }

    suspend fun getProductById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull()
        return if (id != null) {
            val product = productRepository.findById(id)
            if (product != null) {
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(product)
            } else {
                ServerResponse.notFound().buildAndAwait()
            }
        } else {
            ServerResponse.badRequest().buildAndAwait()
        }
    }

    suspend fun createProduct(request: ServerRequest): ServerResponse {
        val product = request.awaitBody<Product>()
        val savedProduct = productRepository.save(product)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(savedProduct)
    }

    suspend fun updateProduct(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val updatedProduct = request.awaitBody<Product>().copy(id = id)
        val savedProduct = productRepository.save(updatedProduct)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(savedProduct)
    }

    suspend fun deleteProduct(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        productRepository.deleteById(id)
        return ServerResponse.noContent().buildAndAwait()
    }

}