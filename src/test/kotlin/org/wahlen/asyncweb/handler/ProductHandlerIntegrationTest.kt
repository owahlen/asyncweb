package org.wahlen.asyncweb.handler

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.wahlen.asyncweb.dto.ProductCreateDTO
import org.wahlen.asyncweb.dto.ProductResponseDTO
import org.wahlen.asyncweb.dto.ProductUpdateDTO
import org.wahlen.asyncweb.service.DatabaseInitializationService

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductHandlerIntegrationTest(@Autowired val webTestClient: WebTestClient) {

    @Autowired
    private lateinit var databaseInitializationService: DatabaseInitializationService

    @BeforeEach
    fun setup() {
        runBlocking {
            databaseInitializationService.cleanDatabase()
            databaseInitializationService.initializeDevelopment()
        }
    }

    @Test
    fun `should return all products`() {
        webTestClient.get().uri("/product")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ProductResponseDTO::class.java)
            .hasSize(3)
    }

    @Test
    fun `should return product details by ID`() {
        webTestClient.get().uri("/product/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(ProductResponseDTO::class.java)
            .isEqualTo(
                ProductResponseDTO(
                    id = 1,
                    categoryId = 1,
                    categoryName = "books",
                    name = "The Pragmatic Programmer",
                    price = 30.0
                )
            )
    }

    @Test
    fun `should create a new product`() {
        val newProduct = ProductCreateDTO(name = "The Lord of the Rings", categoryId = 1, price = 19.9)

        webTestClient.post().uri("/product")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newProduct)
            .exchange()
            .expectStatus().isOk
            .expectBody(ProductResponseDTO::class.java)
            .isEqualTo(
                ProductResponseDTO(
                    id = 4,
                    categoryId = 1,
                    categoryName = "books",
                    name = "The Lord of the Rings",
                    price = 19.9
                )
            )
    }

    @Test
    fun `should update an existing product`() {
        val updatedProduct =
            ProductUpdateDTO(name = "The Pragmatic Programmer - 2nd Edition", categoryId = 1, price = 25.0)

        webTestClient.put().uri("/product/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updatedProduct)
            .exchange()
            .expectStatus().isOk
            .expectBody(ProductResponseDTO::class.java)
            .isEqualTo(
                ProductResponseDTO(
                    id = 1,
                    categoryId = 1,
                    categoryName = "books",
                    name = "The Pragmatic Programmer - 2nd Edition",
                    price = 25.0
                )
            )
    }

    @Test
    fun `should delete a product`() {
        webTestClient.delete().uri("/product/1")
            .exchange()
            .expectStatus().isNoContent
    }
}