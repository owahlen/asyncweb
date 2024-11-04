package org.wahlen.asyncweb.service

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.wahlen.asyncweb.model.Product
import org.wahlen.asyncweb.repository.ProductRepository

@Service
class ProductService(private val productRepository: ProductRepository) {

    @Transactional
    suspend fun createProduct(product: Product): Product {
        // Custom business logic if needed
        return productRepository.save(product)
    }

    suspend fun getProductById(id: Long): Product? {
        return productRepository.findById(id)
    }

    fun getAllProducts(): Flow<Product> {
        return productRepository.findAll()
    }

    @Transactional
    suspend fun updateProduct(product: Product): Product {
        // Custom logic for updating a product can be added here
        return productRepository.save(product)
    }

    @Transactional
    suspend fun deleteProductById(id: Long) {
        productRepository.deleteById(id)
    }
}