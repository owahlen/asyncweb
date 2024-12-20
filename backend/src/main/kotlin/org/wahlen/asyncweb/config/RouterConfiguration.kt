package org.wahlen.asyncweb.config

import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.coRouter
import org.wahlen.asyncweb.handler.CategoryHandler
import org.wahlen.asyncweb.handler.ProductHandler

@Configuration
class RouterConfiguration {

    @Bean
    @RouterOperations(
        RouterOperation(path = "/product", method = arrayOf(RequestMethod.GET), beanClass = ProductHandler::class, beanMethod = "getAllProducts"),
        RouterOperation(path = "/product/{id}", method = arrayOf(RequestMethod.GET), beanClass = ProductHandler::class, beanMethod = "getProductById"),
        RouterOperation(path = "/product", method = arrayOf(RequestMethod.POST), beanClass = ProductHandler::class, beanMethod = "createProduct"),
        RouterOperation(path = "/product/{id}", method = arrayOf(RequestMethod.PUT), beanClass = ProductHandler::class, beanMethod = "updateProduct"),
        RouterOperation(path = "/product/{id}", method = arrayOf(RequestMethod.DELETE), beanClass = ProductHandler::class, beanMethod = "deleteProduct")
    )
    fun productRoutes(productHandler: ProductHandler) = coRouter {
        "/product".nest {
            GET("", productHandler::getAllProducts)
            GET("/{id}", productHandler::getProductById)
            POST("", productHandler::createProduct)
            PUT("/{id}", productHandler::updateProduct)
            DELETE("/{id}", productHandler::deleteProduct)
        }
    }

    @Bean
    @RouterOperations(
        RouterOperation(path = "/category", method = arrayOf(RequestMethod.GET), beanClass = CategoryHandler::class, beanMethod = "getAllCategories"),
        RouterOperation(path = "/category/{id}", method = arrayOf(RequestMethod.GET), beanClass = CategoryHandler::class, beanMethod = "getCategoryById"),
        RouterOperation(path = "/category", method = arrayOf(RequestMethod.POST), beanClass = CategoryHandler::class, beanMethod = "createCategory"),
        RouterOperation(path = "/category/{id}", method = arrayOf(RequestMethod.PUT), beanClass = CategoryHandler::class, beanMethod = "updateCategory"),
        RouterOperation(path = "/category/{id}", method = arrayOf(RequestMethod.DELETE), beanClass = CategoryHandler::class, beanMethod = "deleteCategory")
    )
    fun categoryRoutes(categoryHandler: CategoryHandler) = coRouter {
        "/category".nest {
            GET("", categoryHandler::getAllCategories)
            GET("/{id}", categoryHandler::getCategoryById)
            POST("", categoryHandler::createCategory)
            PUT("/{id}", categoryHandler::updateCategory)
            DELETE("/{id}", categoryHandler::deleteCategory)
        }
    }

}