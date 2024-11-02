package org.wahlen.asyncweb.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter
import org.wahlen.asyncweb.handler.ProductHandler

@Configuration
class RouterConfiguration  {

    @Bean
    fun routes(productHandler: ProductHandler) = coRouter {
        "/product".nest {
            GET("", productHandler::getAllProducts)
            GET("/{id}", productHandler::getProductById)
            POST("", productHandler::createProduct)
            PUT("/{id}", productHandler::updateProduct)
            DELETE("/{id}", productHandler::deleteProduct)
        }
    }
}