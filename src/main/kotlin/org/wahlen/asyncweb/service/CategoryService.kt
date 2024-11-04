package org.wahlen.asyncweb.service

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.wahlen.asyncweb.model.Category
import org.wahlen.asyncweb.repository.CategoryRepository

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    @Transactional
    suspend fun createCategory(category: Category): Category {
        // Custom business logic if needed
        return categoryRepository.save(category)
    }

    suspend fun getCategoryById(id: Long): Category? {
        return categoryRepository.findById(id)
    }

    fun getAllCategories(): Flow<Category> {
        return categoryRepository.findAll()
    }

    @Transactional
    suspend fun updateCategory(category: Category): Category {
        // Custom logic for updating a category can be added here
        return categoryRepository.save(category)
    }

    @Transactional
    suspend fun deleteCategoryById(id: Long) {
        categoryRepository.deleteById(id)
    }
}