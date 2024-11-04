package org.wahlen.asyncweb.handler

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductHandlerTest(@Autowired val webTestClient: WebTestClient) {

    @MockBean
    private lateinit var productRepository: ProductRepository

    @Test
    fun `should return all products`(): Unit = runBlocking {
        whenever(productRepository.findAll()).thenReturn(
            flowOf(
                Product(id = 1, categoryId = 0, name = "Product A", price = 10.0),
                Product(id = 2, categoryId = 0, name = "Product B", price = 20.0)
            )
        )

        webTestClient.get().uri("/product")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Product::class.java)
            .hasSize(2)
    }

    @Test
    fun `should return product details by ID`(): Unit = runBlocking {
        val product = Product(id = 1, categoryId = 0, name = "Product A", price = 10.0)

        whenever(productRepository.findById(1)).thenReturn(product)

        webTestClient.get().uri("/product/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Product::class.java)
            .isEqualTo(product)
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
        val newProduct = Product(name = "Product C", categoryId = 0, price = 30.0)
        val savedProduct = Product(id = 3, categoryId = 0, name = "Product C", price = 30.0)

        whenever(productRepository.save(newProduct)).thenReturn(savedProduct)

        webTestClient.post().uri("/product")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newProduct)
            .exchange()
            .expectStatus().isOk
            .expectBody(Product::class.java)
            .isEqualTo(savedProduct)
    }

    @Test
    fun `should update an existing product`(): Unit = runBlocking {
        val updatedProduct = Product(id = 1, categoryId = 0, name = "Updated Product", price = 15.0)

        whenever(productRepository.save(updatedProduct)).thenReturn(updatedProduct)

        webTestClient.put().uri("/product/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updatedProduct)
            .exchange()
            .expectStatus().isOk
            .expectBody(Product::class.java)
            .isEqualTo(updatedProduct)
    }

    @Test
    fun `should delete a product`(): Unit = runBlocking {
        doAnswer {}.whenever(productRepository).deleteById(1)

        webTestClient.delete().uri("/product/1")
            .exchange()
            .expectStatus().isNoContent
    }
}
