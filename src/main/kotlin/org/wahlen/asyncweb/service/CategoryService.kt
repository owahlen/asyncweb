package org.wahlen.asyncweb.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.wahlen.asyncweb.dto.*
import org.wahlen.asyncweb.model.Category
import org.wahlen.asyncweb.repository.CategoryRepository

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun getAllCategoryResponseDTOs(): Flow<CategoryResponseDTO> {
        return categoryRepository.findAll().map { it.toCategoryResponseDTO() }
    }

    suspend fun getCategoryResponseDTOById(id: Long): CategoryResponseDTO {
        return getCategoryById(id).toCategoryResponseDTO()
    }

    suspend fun getCategoryById(id: Long): Category {
        return categoryRepository.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id $id not found")
    }

    @Transactional
    suspend fun createCategory(categoryCreateDTO: CategoryCreateDTO): CategoryResponseDTO {
        val category = categoryCreateDTO.toCategory()
        categoryRepository.findByName(category.name)?.let {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Category '${category.name}' already exists")
        }
        return categoryRepository.save(category).toCategoryResponseDTO()
    }


    @Transactional
    suspend fun updateCategory(id: Long, categoryUpdateDTO: CategoryUpdateDTO): CategoryResponseDTO {
        val category = getCategoryById(id)
        val updatedCategory = categoryUpdateDTO.toCategory(category)
        categoryRepository.findByName(updatedCategory.name)?.takeIf { it.id != updatedCategory.id }?.let {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Category '${updatedCategory.name}' already exists")
        }
        return categoryRepository.save(updatedCategory).toCategoryResponseDTO()
    }

    @Transactional
    suspend fun deleteCategoryById(id: Long) {
        getCategoryById(id)
        categoryRepository.deleteById(id)
    }
}