plugins {
    kotlin("jvm") version "1.9.22"
    alias(libs.plugins.shadow)
    alias(libs.plugins.paper.yml)
}

group = "de.daver.unigate"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "paper"
    }
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(kotlin("stdlib"))
}