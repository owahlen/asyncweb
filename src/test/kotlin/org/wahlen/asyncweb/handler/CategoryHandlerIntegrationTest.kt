package org.wahlen.asyncweb.handler

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.wahlen.asyncweb.dto.CategoryCreateDTO
import org.wahlen.asyncweb.dto.CategoryResponseDTO
import org.wahlen.asyncweb.dto.CategoryUpdateDTO
import org.wahlen.asyncweb.dto.toCategoryResponseDTO
import org.wahlen.asyncweb.repository.CategoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatList
import org.wahlen.asyncweb.model.Category

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryHandlerIntegrationTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val categoryRepository: CategoryRepository
) {

    @Test
    fun `should return all categories`() {
        runBlocking {
            // setup
            val category = categoryRepository.findByName("books")!!
            val categoryResponseDTO = category.toCategoryResponseDTO()

            // when
            val dtoList = webTestClient.get().uri("/category")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBodyList(CategoryResponseDTO::class.java)
                .returnResult().responseBody!!

            // then
            assertThatList(dtoList).isEqualTo(listOf(categoryResponseDTO))
        }
    }

    @Test
    fun `should return category details by ID`() {
        runBlocking {
            // setup
            val category = categoryRepository.findByName("books")!!

            // when
            val dto = webTestClient.get().uri("/category/${category.id!!}")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody(CategoryResponseDTO::class.java)
                .returnResult().responseBody!!

            // then
            assertThat(dto.name).isEqualTo("books")
        }
    }

    @Test
    fun `should create a new category`() {
        runBlocking {
            // setup
            val categoryCreateDTO = CategoryCreateDTO(name = "shoes")

            // when
            val dto = webTestClient.post().uri("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryCreateDTO)
                .exchange()
                .expectStatus().isOk
                .expectBody(CategoryResponseDTO::class.java)
                .returnResult().responseBody!!

            // then
            assertThat(dto.name).isEqualTo("shoes")
            assertThat(categoryRepository.findByName("shoes")).isNotNull()

            // cleanup
            categoryRepository.deleteById(dto.id)
        }
    }

    @Test
    fun `should update an existing category`() {
        runBlocking {
            // setup
            val category = categoryRepository.findByName("books")!!
            val categoryUpdateDTO = CategoryUpdateDTO(name = "literature")

            // when
            val dto = webTestClient.put().uri("/category/${category.id!!}")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryUpdateDTO)
                .exchange()
                .expectStatus().isOk
                .expectBody(CategoryResponseDTO::class.java)
                .returnResult().responseBody!!

            // then
            assertThat(dto.name).isEqualTo("literature")
            val updatedCategory = categoryRepository.findById(category.id!!)
            assertThat(updatedCategory).isNotNull()
            assertThat(updatedCategory!!.name).isEqualTo(dto.name)

            // cleanup
            categoryRepository.save(category)
        }
    }

    @Test
    fun `should delete a category`() {
        runBlocking {
            // setup
            val createdCategory = categoryRepository.save(Category(name = "clothes"))

            // when
            webTestClient.delete().uri("/category/${createdCategory.id!!}")
                .exchange()
                .expectStatus().isNoContent

            // then
            assertThat(categoryRepository.findById(createdCategory.id!!)).isNull()
        }
    }

}