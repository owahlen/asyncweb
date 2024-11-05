package org.wahlen.asyncweb.handler

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.wahlen.asyncweb.dto.ProductCreateDTO
import org.wahlen.asyncweb.dto.ProductResponseDTO
import org.wahlen.asyncweb.dto.ProductUpdateDTO
import org.wahlen.asyncweb.dto.toProductResponseDTO
import org.wahlen.asyncweb.model.Product
import org.wahlen.asyncweb.repository.CategoryRepository
import org.wahlen.asyncweb.repository.ProductRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductHandlerIntegrationTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val categoryRepository: CategoryRepository,
    @Autowired private val productRepository: ProductRepository
) {

    @Test
    fun `should return all products`() {
        runBlocking {
            // setup
            val productResponseDTOs = productRepository.findAll().map { product ->
                val category = categoryRepository.findById(product.categoryId)!!
                product.toProductResponseDTO(category)
            }

            // when
            val dtoList = webTestClient.get().uri("/product")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBodyList(ProductResponseDTO::class.java)
                .returnResult().responseBody!!

            // then
            assertThat(dtoList.toSet()).isEqualTo(productResponseDTOs.toSet())
        }
    }

    @Test
    fun `should return product details by ID`() {
        runBlocking {
            // setup
            val product = productRepository.findByName("The Pragmatic Programmer")!!

            // when
            val dto = webTestClient.get().uri("/product/${product.id!!}")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody(ProductResponseDTO::class.java)
                .returnResult().responseBody!!

            // then
            assertThat(dto.name).isEqualTo("The Pragmatic Programmer")
        }
    }

    @Test
    fun `should create a new product`() {
        runBlocking {
            // setup
            val productCreateDTO = ProductCreateDTO(name = "Design Patterns", categoryId = 1, price = 29.99)

            // when
            val dto = webTestClient.post().uri("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productCreateDTO)
                .exchange()
                .expectStatus().isOk
                .expectBody(ProductResponseDTO::class.java)
                .returnResult().responseBody!!

            // then
            assertThat(dto.name).isEqualTo("Design Patterns")
            assertThat(productRepository.findByName("Design Patterns")).isNotNull()

            // cleanup
            productRepository.deleteById(dto.id)
        }
    }

    @Test
    fun `should update an existing product`() {
        runBlocking {
            // setup
            val product = productRepository.findByName("The Pragmatic Programmer")!!
            val productUpdateDTO = ProductUpdateDTO(name = "The Pragmatic Programmer - 2nd Edition", categoryId = 1, price = 35.0)

            // when
            val dto = webTestClient.put().uri("/product/${product.id!!}")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productUpdateDTO)
                .exchange()
                .expectStatus().isOk
                .expectBody(ProductResponseDTO::class.java)
                .returnResult().responseBody!!

            // then
            assertThat(dto.name).isEqualTo("The Pragmatic Programmer - 2nd Edition")
            val updatedProduct = productRepository.findById(product.id!!)
            assertThat(updatedProduct).isNotNull()
            assertThat(updatedProduct!!.name).isEqualTo(dto.name)

            // cleanup
            productRepository.save(product)
        }
    }

    @Test
    fun `should delete a product`() {
        runBlocking {
            // setup
            val createdProduct = productRepository.save(Product(name = "Refactoring", categoryId = 1, price = 49.99))

            // when
            webTestClient.delete().uri("/product/${createdProduct.id!!}")
                .exchange()
                .expectStatus().isNoContent

            // then
            assertThat(productRepository.findById(createdProduct.id!!)).isNull()
        }
    }
}