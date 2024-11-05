package org.wahlen.asyncweb.config

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.wahlen.asyncweb.service.DatabaseInitializationService

@Configuration
class DatabaseInitializerConfiguration(
    private val databaseInitializationService: DatabaseInitializationService
) {
    @Bean
    fun productionDatabaseInitializer() = InitializingBean {
        runBlocking {
            databaseInitializationService.initializeProduction()
        }
    }

    @Bean
    @DependsOn("productionDatabaseInitializer")
    @Profile("development", "test")
    fun developmentDatabaseInitializer() = InitializingBean {
        runBlocking {
            databaseInitializationService.initializeDevelopment()
        }
    }
}