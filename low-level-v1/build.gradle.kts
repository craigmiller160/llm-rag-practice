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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("io.milvus:milvus-sdk-java:2.3.4")
    implementation(project(":libraries:common-spring"))
    implementation(project(":libraries:common-spring-openai"))
    implementation(project(":libraries:common-milvus"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

configure<SpotlessExtension> {
    kotlin {
        ktfmt("0.43")
    }
}