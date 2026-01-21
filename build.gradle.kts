import net.minecrell.pluginyml.paper.PaperPluginDescription


plugins {
    kotlin("jvm") version "1.9.22"
    alias(libs.plugins.shadow)
    alias(libs.plugins.paper.yml)
}

group = "de.daver.unigate"
version = "1.2.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "paper"
    }
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.luckPerms)
    implementation(libs.nbt)
    implementation(libs.hikariCP)
    implementation(libs.apache.compess)
}

paper {
    apiVersion = "1.21"
    main = "de.daver.unigate.UniversalGatePlugin"

    serverDependencies {
        register("LuckPerms") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}

tasks.register<Copy>("copyJar") {
    dependsOn(tasks.shadowJar)

    from(layout.buildDirectory.dir("libs"))

    into(layout.projectDirectory.dir("debug_server/plugins"))
    include("*-all.jar")
}
