plugins {
    id("net.fabricmc.fabric-loom-remap")
    `maven-publish`
}

version = "${property("mod.version")}+${sc.current.version}"
group = property("mod.group") as String
base.archivesName = property("mod.id") as String

val requiredJava = when {
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
    else -> JavaVersion.VERSION_1_8
}

repositories {
    maven("https://masa.dy.fi/maven") {
        name = "Masa"
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    mappings(loom.officialMojangMappings())
    add("modImplementation", "net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    add("modImplementation", "carpet:fabric-carpet:${property("deps.carpet")}")
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
    sourceCompatibility = requiredJava
    targetCompatibility = requiredJava
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
            expand(mapOf("java" to "JAVA_${requiredJava.majorVersion}"))
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
        from(named<AbstractArchiveTask>("remapJar").flatMap { it.archiveFile })
        from(named<AbstractArchiveTask>("remapSourcesJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }

    register<Copy>("buildAndCollectRemapped") {
        group = "build"
        from(named<AbstractArchiveTask>("remapJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod.version")}/remapped"))
        dependsOn("build")
    }

    register<Copy>("buildAndCollectSources") {
        group = "build"
        from(named<AbstractArchiveTask>("remapSourcesJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod.version")}/sources"))
        dependsOn("build")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.named("remapJar"))
            artifact(tasks.named("remapSourcesJar"))
        }
    }
}
