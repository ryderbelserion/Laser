plugins {
    `config-publish`
    `config-velocity`
}

project.group = "${rootProject.name}.velocity"

dependencies {
    api(project(":laser-core"))
}