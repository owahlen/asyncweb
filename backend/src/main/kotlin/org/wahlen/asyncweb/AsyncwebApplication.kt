package org.wahlen.asyncweb

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(
    info = Info(
        title = "Asyncweb API",
        version = "0.0.1-SNAPSHOT",
        description = "Asyncweb API using Spring WebFlux and R2DBC"
    )
)
class AsyncwebApplication

fun main(args: Array<String>) {
    runApplication<AsyncwebApplication>(*args)
}
