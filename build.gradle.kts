val projectGroup: String by project
val projectVersion: String by project

allprojects {
    version = projectVersion
    group = projectGroup
}

plugins {
    val kotlinVersion = "2.0.0-Beta4"

    kotlin("jvm") version kotlinVersion apply false
}