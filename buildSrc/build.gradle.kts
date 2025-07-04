import dev.extframework.gradle.common.archives
import dev.extframework.gradle.common.commonUtil
import dev.extframework.gradle.common.dm.resourceApi
import dev.extframework.gradle.common.extFramework

plugins {
    `kotlin-dsl`
    id("dev.extframework.common") version "1.0.45"
    publishing
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    extFramework()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.19.0")
    resourceApi()
    commonUtil()
    archives()
}

common {
    defaultJavaSettings()
}



