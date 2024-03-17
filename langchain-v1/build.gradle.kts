import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    val kotlinVersion = "2.0.0-Beta4"

    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("io.craigmiller160.gradle.defaults") version "1.2.2"
    id("com.diffplug.spotless") version "6.17.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_20
}

dependencies {
    val langchainVersion = "0.28.0"

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("dev.langchain4j:langchain4j:$langchainVersion")
    implementation("dev.langchain4j:langchain4j-milvus:$langchainVersion")
    implementation("dev.langchain4j:langchain4j-embeddings-all-minilm-l6-v2:$langchainVersion")
    implementation("dev.langchain4j:langchain4j-chroma:$langchainVersion")
    implementation("dev.langchain4j:langchain4j-open-ai:$langchainVersion")
    implementation(project(":libraries:common-spring"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "20"
    }
}

configure<SpotlessExtension> {
    kotlin {
        ktfmt("0.43")
    }
}