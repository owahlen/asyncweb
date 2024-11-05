package org.wahlen.asyncweb.service

import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.wahlen.asyncweb.dto.*
import org.wahlen.asyncweb.model.Product
import org.wahlen.asyncweb.repository.CategoryRepository
import org.wahlen.asyncweb.repository.ProductRepository

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {

    fun getAllProductResponseDTOs(): Flow<ProductResponseDTO> {
        return productRepository.findAllProductResponseDTOs()
    }

    @Transactional(readOnly = true)
    suspend fun getProductResponseDTOById(id: Long): ProductResponseDTO {
        val product = getProductById(id)
        val category = categoryRepository.findById(product.categoryId)!!
        return product.toProductResponseDTO(category)
    }

    suspend fun getProductById(id: Long): Product {
        return productRepository.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id $id not found")
    }

    @Transactional
    suspend fun createProduct(productCreateDTO: ProductCreateDTO): ProductResponseDTO {
        val newProduct = productCreateDTO.toProduct()
        val categoryId = newProduct.categoryId
        val category = categoryRepository.findById(categoryId)
            ?: throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Category with id ${categoryId} not found")
        return productRepository.save(newProduct).toProductResponseDTO(category)
    }

    @Transactional
    suspend fun updateProduct(id: Long, productUpdateDTO: ProductUpdateDTO): ProductResponseDTO {
        val existingProduct = getProductById(id)
        val categoryId = existingProduct.categoryId
        val category = categoryRepository.findById(categoryId)
            ?: throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Category with id ${categoryId} not found")
        val updatedProduct = productUpdateDTO.toProduct().copy(id = existingProduct.id)
        return productRepository.save(updatedProduct).toProductResponseDTO(category)
    }

    @Transactional
    suspend fun deleteProductById(id: Long) {
        getProductById(id)
        productRepository.deleteById(id)
    }
}