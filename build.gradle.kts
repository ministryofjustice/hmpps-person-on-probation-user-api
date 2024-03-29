plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "5.14.1"
  kotlin("plugin.spring") version "1.9.22"
  id("org.springdoc.openapi-gradle-plugin") version "1.6.0"
  id("jacoco")
  id("org.sonarqube") version "4.0.0.2929"
  kotlin("plugin.jpa") version "1.8.22"
  kotlin("plugin.serialization") version "1.9.22"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

repositories {
  maven { url = uri("https://repo.spring.io/milestone") }
  mavenCentral()
}

dependencies {
  implementation("javax.servlet:javax.servlet-api:4.0.1")

  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("org.projectlombok:lombok:1.18.26")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

  runtimeOnly("org.flywaydb:flyway-core")
  runtimeOnly("org.springframework.boot:spring-boot-starter-jdbc")
  runtimeOnly("org.postgresql:postgresql:42.6.0")

  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
  implementation("io.opentelemetry:opentelemetry-api:1.28.0")
  implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:1.28.0")
  implementation("io.micrometer:micrometer-registry-prometheus:1.11.4")

  implementation("org.apache.commons:commons-lang3")
  implementation("org.apache.commons:commons-text:1.10.0")
  implementation("commons-codec:commons-codec")
  implementation("com.google.code.gson:gson")
  implementation("org.json:json:20231013")

  implementation("com.pauldijou:jwt-core_2.11:5.0.0")

  developmentOnly("org.springframework.boot:spring-boot-devtools")

  testImplementation("org.awaitility:awaitility-kotlin")
  testImplementation("io.jsonwebtoken:jjwt-impl:0.11.5")
  testImplementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
  testImplementation("org.mockito:mockito-inline:4.6.1")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.19")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:2.35.1")
  testImplementation("org.testcontainers:localstack:1.18.1")
  testImplementation("org.testcontainers:postgresql:1.18.1")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
  testImplementation("javax.xml.bind:jaxb-api:2.3.1")
  testImplementation("io.opentelemetry:opentelemetry-sdk-testing:1.34.1")
  testImplementation("com.squareup.okhttp3:okhttp:4.12.0")
  testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
  testImplementation("io.mockk:mockk:1.13.7")

  if (project.hasProperty("docs")) {
    implementation("com.h2database:h2")
  }
}

kotlin {
  jvmToolchain(21)
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "21"
    }
  }
}
