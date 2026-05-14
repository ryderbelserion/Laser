pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Laser"

listOf(
    "example",

    "paper",
    "core"
).forEach {
    includeProject(it)
}

fun includeProject(name: String) {
    includeProject(name) {
        this.name = "${rootProject.name.lowercase()}-$name"
    }
}

fun includeProject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name)
    project(":$name").apply(block)
}