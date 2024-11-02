package org.wahlen.asyncweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AsyncwebApplication

fun main(args: Array<String>) {
	runApplication<AsyncwebApplication>(*args)
}
