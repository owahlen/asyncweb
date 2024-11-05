package org.wahlen.asyncweb.handler

import org.wahlen.asyncweb.dto.CategoryResponseDTO
import org.wahlen.asyncweb.model.Category
import org.wahlen.asyncweb.repository.CategoryRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryHandlerTest(@Autowired val webTestClient: WebTestClient) {

    @MockBean
    private lateinit var categoryRepository: CategoryRepository

    @Test
    fun `should return all categories`(): Unit = runBlocking {
        whenever(categoryRepository.findAll()).thenReturn(
            flowOf(
                Category(id = 1, name = "Category A"),
                Category(id = 2, name = "Category B")
            )
        )

        webTestClient.get().uri("/category")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CategoryResponseDTO::class.java)
            .hasSize(2)
            .contains(
                CategoryResponseDTO(id = 1, name = "Category A"),
                CategoryResponseDTO(id = 2, name = "Category B")
            )
    }

    @Test
    fun `should return category details by ID`(): Unit = runBlocking {
        val category = Category(id = 1, name = "Category A")

        whenever(categoryRepository.findById(1)).thenReturn(category)

        webTestClient.get().uri("/category/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(CategoryResponseDTO::class.java)
            .isEqualTo(CategoryResponseDTO(id = 1, name = "Category A"))
    }

    @Test
    fun `should return 404 if category not found`(): Unit = runBlocking {
        whenever(categoryRepository.findById(1)).thenReturn(null)

        webTestClient.get().uri("/category/1")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should create a new category`(): Unit = runBlocking {
        val newCategory = Category(name = "Category C")
        val savedCategory = Category(id = 3, name = "Category C")

        whenever(categoryRepository.save(newCategory)).thenReturn(savedCategory)

        webTestClient.post().uri("/category")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newCategory)
            .exchange()
            .expectStatus().isOk
            .expectBody(CategoryResponseDTO::class.java)
            .isEqualTo(CategoryResponseDTO(id = 3, name = "Category C"))
    }

    @Test
    fun `should update an existing category`(): Unit = runBlocking {
        val category = Category(id = 1, name = "Category A")
        val updatedCategory = Category(id = 1, name = "Updated Category")

        whenever(categoryRepository.findById(1)).thenReturn(category)
        whenever(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory)

        webTestClient.put().uri("/category/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updatedCategory)
            .exchange()
            .expectStatus().isOk
            .expectBody(CategoryResponseDTO::class.java)
            .isEqualTo(CategoryResponseDTO(id = 1, name = "Updated Category"))
    }

    @Test
    fun `should delete a category`(): Unit = runBlocking {
        val category = Category(id = 1, name = "Category A")
        whenever(categoryRepository.findById(1)).thenReturn(category)
        doAnswer {}.whenever(categoryRepository).deleteById(1)

        webTestClient.delete().uri("/category/1")
            .exchange()
            .expectStatus().isNoContent
    }
}
