import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    val kotlinVersion = "2.0.0-Beta4"

    kotlin("jvm") version kotlinVersion
    id("io.craigmiller160.gradle.defaults") version "1.2.2"
    id("com.diffplug.spotless") version "6.17.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly("io.milvus:milvus-sdk-java:2.3.4")
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