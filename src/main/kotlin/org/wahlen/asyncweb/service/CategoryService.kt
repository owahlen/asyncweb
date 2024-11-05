package org.wahlen.asyncweb.service

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.wahlen.asyncweb.dto.CategoryCreateDTO
import org.wahlen.asyncweb.dto.CategoryUpdateDTO
import org.wahlen.asyncweb.dto.toCategory
import org.wahlen.asyncweb.model.Category
import org.wahlen.asyncweb.repository.CategoryRepository

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    @Transactional
    suspend fun createCategory(categoryCreateDTO: CategoryCreateDTO): Category {
        val category = categoryCreateDTO.toCategory()
        return categoryRepository.save(category)
    }

    suspend fun getCategoryById(id: Long): Category {
        return categoryRepository.findById(id) ?: throw NoSuchElementException("Category $id not found")
    }

    fun getAllCategories(): Flow<Category> {
        return categoryRepository.findAll()
    }

    @Transactional
    suspend fun updateCategory(id:Long, categoryUpdateDTO: CategoryUpdateDTO): Category {
        // Custom logic for updating a category can be added here
        val category = categoryRepository.findById(id) ?: throw NoSuchElementException("Category $id not found")
        val updatedCategory = categoryUpdateDTO.toCategory(category)
        return categoryRepository.save(updatedCategory)
    }

    @Transactional
    suspend fun deleteCategoryById(id: Long) {
        categoryRepository.findById(id) ?: throw NoSuchElementException("Category $id not found")
        categoryRepository.deleteById(id)
    }
}