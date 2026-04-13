plugins {
    id("java")
}

group = "de.daver.unigate"
version = "1.5.4"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "paper"
    }
}

dependencies {
    compileOnly(libs.paper.api)
}
