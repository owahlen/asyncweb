package org.wahlen.asyncweb.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

@Configuration
class WebExceptionHandlerConfiguration(private val objectMapper: ObjectMapper) {

    var logger: Logger = LoggerFactory.getLogger(WebExceptionHandlerConfiguration::class.java)

    @Bean
    @Order(-2)
    fun exceptionHandler() = WebExceptionHandler { exchange: ServerWebExchange, ex: Throwable ->
        when (ex) {
            is ConstraintViolationException -> {
                writeResponse(exchange, HttpStatus.BAD_REQUEST, ex.message)
            }
            is IllegalArgumentException -> {
                writeResponse(exchange, HttpStatus.BAD_REQUEST, ex.message)
            }
            is ResponseStatusException -> {
                val httpStatus = HttpStatus.valueOf(ex.statusCode.value())
                writeResponse(exchange, httpStatus, ex.reason)
            }

            else -> {
                logger.error("Internal Server Error", ex)
                writeResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "")
            }
        }
    }

    private fun writeResponse(exchange: ServerWebExchange, httpStatus: HttpStatus, message: String?): Mono<Void> {
        val timestamp = System.currentTimeMillis()
        val path = exchange.request.path.value()
        val jsonError = createJSONError(timestamp, path, httpStatus, message ?: "")
        val response = exchange.response
        response.statusCode = httpStatus
        response.headers.contentType = MediaType.APPLICATION_JSON
        val buffer = response.bufferFactory().wrap(jsonError)
        return response.writeWith(Mono.just(buffer))
    }

    private fun createJSONError(timestamp: Long, path: String, httpStatus: HttpStatus, message: String): ByteArray {
        val status = httpStatus.value()
        val error = httpStatus.reasonPhrase
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(
            mapOf(
                "timestamp" to timestamp,
                "status" to status,
                "error" to error,
                "path" to path,
                "message" to message
            )
        )
    }

}
