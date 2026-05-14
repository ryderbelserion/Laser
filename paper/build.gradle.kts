plugins {
    `config-publish`
    `config-paper`
}

project.group = "${rootProject.name}.paper"

dependencies {
    api(project(":laser-core"))
}