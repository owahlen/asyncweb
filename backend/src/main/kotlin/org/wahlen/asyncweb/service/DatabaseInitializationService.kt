package org.wahlen.asyncweb.service

import kotlinx.coroutines.flow.firstOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.wahlen.asyncweb.model.Category
import org.wahlen.asyncweb.model.Product
import org.wahlen.asyncweb.repository.CategoryRepository
import org.wahlen.asyncweb.repository.ProductRepository

@Service
@Transactional
class DatabaseInitializationService(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) {

    suspend fun initializeProduction() {
    }

    suspend fun initializeDevelopment() {
        createCategories()
        createProducts()
    }

    suspend fun cleanDatabase() {
        productRepository.deleteAll()
        categoryRepository.deleteAll()
    }

    suspend fun createCategories() {
        listOf(
            Category(name = "books")
        ).forEach {
            categoryRepository.findByName(it.name) ?: run { categoryRepository.save(it) }
        }
    }

    suspend fun createProducts() {
        val products = productRepository.findAll()
        val categoryId = categoryRepository.findByName("books")!!.id!!
        listOf(
            Product(categoryId = categoryId, name = "The Pragmatic Programmer", price = 30.0),
            Product(categoryId = categoryId, name = "Clean Code", price = 40.0),
            Product(categoryId = categoryId, name = "Refactoring", price = 50.0)
        ).forEach { newProduct ->
            products.firstOrNull { it.name == newProduct.name } ?: run { productRepository.save(newProduct) }
        }
    }

}