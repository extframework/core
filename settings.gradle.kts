pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.extframework.dev/releases")
        }
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "core"

include(":tests")

include("mixin")
include("main")
include("app")
include("entrypoint")
include("instrument")
include("capability")
include("app:api")
findProject(":app:api")?.name = "app-api"
include("minecraft")
include("minecraft:api")
findProject(":minecraft:api")?.name = "minecraft-api"

include(":minecraft:blackbox")
findProject(":minecraft:blackbox")?.projectDir = file("minecraft/tests/blackbox")

include(":minecraft:app")
findProject(":minecraft:app")?.run {
    projectDir = file("minecraft/tests/app")
    name = "blackbox-app"
}