plugins {
    id("net.fabricmc.fabric-loom")
    `maven-publish`
}

version = "${property("mod.version")}+${sc.current.version}"
group = property("mod.group") as String
base.archivesName = property("mod.id") as String

repositories {
    maven("https://masa.dy.fi/maven") {
        name = "Masa"
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    implementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")

    val carpetVersion = property("deps.carpet").toString()
    if (carpetVersion.isNotBlank()) {
        implementation("carpet:fabric-carpet:$carpetVersion")
    }
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")

    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../run"
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks {
    processResources {
        val props = mapOf(
            "id" to project.property("mod.id"),
            "name" to project.property("mod.name"),
            "version" to project.property("mod.version"),
            "minecraft" to project.property("mod.mc_dep"),
            "fabricLoader" to project.property("build.fabric_loader")
        )

        filesMatching("fabric.mod.json") {
            expand(props)
        }

        filesMatching("*.mixins.json") {
            expand(mapOf("java" to "JAVA_25"))
        }
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    named<AbstractArchiveTask>("jar") {
        from(rootProject.file("LICENSE"))
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(named<AbstractArchiveTask>("jar").flatMap { it.archiveFile })
        from(named<AbstractArchiveTask>("sourcesJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }

    register<Copy>("buildAndCollectRemapped") {
        group = "build"
        from(named<AbstractArchiveTask>("jar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod.version")}/remapped"))
        dependsOn("build")
    }

    register<Copy>("buildAndCollectSources") {
        group = "build"
        from(named<AbstractArchiveTask>("sourcesJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod.version")}/sources"))
        dependsOn("build")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.named("jar"))
            artifact(tasks.named("sourcesJar"))
        }
    }
}
