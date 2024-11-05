package org.wahlen.asyncweb.handler

import org.wahlen.asyncweb.dto.ProductResponseDTO
import org.wahlen.asyncweb.model.Product
import org.wahlen.asyncweb.repository.ProductRepository
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
import org.wahlen.asyncweb.model.Category
import org.wahlen.asyncweb.repository.CategoryRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductHandlerTest(@Autowired val webTestClient: WebTestClient) {

    @MockBean
    private lateinit var categoryRepository: CategoryRepository

    @MockBean
    private lateinit var productRepository: ProductRepository

    @Test
    fun `should return all products`(): Unit = runBlocking {
        whenever(productRepository.findAllProductResponseDTOs()).thenReturn(
            flowOf(
                ProductResponseDTO(id = 1, categoryId = 1, categoryName = "books", name = "Product A", price = 10.0),
                ProductResponseDTO(id = 2, categoryId = 1, categoryName = "books", name = "Product B", price = 20.0)
            )
        )

        webTestClient.get().uri("/product")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ProductResponseDTO::class.java)
            .hasSize(2)
            .contains(
                ProductResponseDTO(id = 1, categoryId = 1, categoryName = "books", name = "Product A", price = 10.0),
                ProductResponseDTO(id = 2, categoryId = 1, categoryName = "books", name = "Product B", price = 20.0)
            )
    }

    @Test
    fun `should return product details by ID`(): Unit = runBlocking {
        val product = Product(id = 1, categoryId = 1, name = "Product A", price = 10.0)
        val category = Category(id = 1, name = "books")

        whenever(productRepository.findById(1)).thenReturn(product)
        whenever(categoryRepository.findById(1)).thenReturn(category)

        webTestClient.get().uri("/product/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(ProductResponseDTO::class.java)
            .isEqualTo(ProductResponseDTO(id = 1, categoryId = 1, categoryName = "books", name = "Product A", price = 10.0))
    }

    @Test
    fun `should return 404 if product not found`(): Unit = runBlocking {
        whenever(productRepository.findById(1)).thenReturn(null)

        webTestClient.get().uri("/product/1")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should create a new product`(): Unit = runBlocking {
        val newProduct = Product(name = "Product C", categoryId = 1, price = 30.0)
        val savedProduct = Product(id = 3, categoryId = 1, name = "Product C", price = 30.0)
        val category = Category(id = 1, name = "books")

        whenever(productRepository.save(newProduct)).thenReturn(savedProduct)
        whenever(categoryRepository.findById(1)).thenReturn(category)

        webTestClient.post().uri("/product")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newProduct)
            .exchange()
            .expectStatus().isOk
            .expectBody(ProductResponseDTO::class.java)
            .isEqualTo(ProductResponseDTO(id = 3, categoryId = 1, categoryName = "books", name = "Product C", price = 30.0))
    }

    @Test
    fun `should update an existing product`(): Unit = runBlocking {
        val product = Product(id = 1, categoryId = 1, name = "Product A", price = 10.0)
        val updatedProduct = Product(id = 1, categoryId = 1, name = "Updated Product", price = 15.0)
        val category = Category(id = 1, name = "books")

        whenever(productRepository.findById(1)).thenReturn(product)
        whenever(categoryRepository.findById(1)).thenReturn(category)
        whenever(productRepository.save(updatedProduct)).thenReturn(updatedProduct)

        webTestClient.put().uri("/product/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updatedProduct)
            .exchange()
            .expectStatus().isOk
            .expectBody(ProductResponseDTO::class.java)
            .isEqualTo(ProductResponseDTO(id = 1, categoryId = 1, categoryName = "books", name = "Updated Product", price = 15.0))
    }

    @Test
    fun `should delete a product`(): Unit = runBlocking {
        val product = Product(id = 1, categoryId = 0, name = "Product A", price = 10.0)
        whenever(productRepository.findById(1)).thenReturn(product)
        doAnswer {}.whenever(productRepository).deleteById(1)

        webTestClient.delete().uri("/product/1")
            .exchange()
            .expectStatus().isNoContent
    }
}
