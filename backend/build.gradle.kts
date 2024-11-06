plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.flywaydb.flyway") version "10.20.1"
}

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:10.20.1")
    }
}

group = "org.wahlen"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

// the "webResources" configuration is resolved to the "webapp" configuration in the webapp project
// https://docs.gradle.org/current/userguide/cross_project_publications.html
val webResources by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    // api
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0") // http://localhost:8080/swagger-ui.html

    // database
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:10.20.1")
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql") // for flyway migrations

    // testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testRuntimeOnly("io.r2dbc:r2dbc-h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//    webResources(
//        // dependency to the webapp configuration in the webapp project
//        project(
//            mapOf(
//                "path" to ":webapp",
//                "configuration" to "webapp"
//            )
//        )
//    )

}

val dbHost = findProperty("db.host") ?: "localhost"
val dbPort = findProperty("db.port") ?: "5432"
val dbName = findProperty("db.name") ?: "asyncweb"
val dbSchema = findProperty("db.schema") ?: "public"
val dbUser = findProperty("db.user") ?: "asyncweb"
val dbPassword = findProperty("db.password") ?: "password"

flyway {
    url = "jdbc:postgresql://${dbHost}:${dbPort}/${dbName}"
    driver = "org.postgresql.Driver"
    user = "$dbUser"
    password = "$dbPassword"
    schemas = arrayOf("$dbSchema")
    cleanDisabled = false
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}

//tasks.processResources {
//    dependsOn(":webapp:yarn_build")
//    copy {
//        from(webResources)
//        // spring boot serves static html from the "/public" folder
//        into(layout.buildDirectory.dir("resources/main/public"))
//    }
//}