pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.1"
}

stonecutter {
    create(rootProject) {
        versions("1.21.11").buildscript("build.obfuscated.gradle.kts")
        versions("26.1.2").buildscript("build.unobfuscated.gradle.kts")

        vcsVersion = "26.1.2"
    }
}

rootProject.name = "template-mod"
