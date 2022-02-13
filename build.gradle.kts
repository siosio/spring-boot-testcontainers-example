import groovy.lang.Closure
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("kapt") version "1.6.10"
}

ext["kotlin-coroutines.version"] = "1.6.0"

group = "siosio"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.flywaydb:flyway-core")
    runtimeOnly("mysql:mysql-connector-java")

    implementation("org.seasar.doma.boot:doma-spring-boot-starter:1.6.0")
    implementation("org.seasar.doma:doma-core:2.51.0")
    kapt("org.seasar.doma:doma-processor:2.51.0")
    compileOnly("org.springframework.boot:spring-boot-devtools")
    // Javaの場合
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    testImplementation(platform("org.testcontainers:testcontainers-bom:1.16.3"))
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:junit-jupiter")

    testImplementation("org.assertj:assertj-db:2.0.2")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.1.0")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.0")
    testImplementation("com.ninja-squad:springmockk:3.1.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
