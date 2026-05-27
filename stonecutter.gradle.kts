plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom-remap") version "1.15-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT" apply false
}

stonecutter active "26.1.2"

stonecutter parameters {
    swaps["mod_version"] = "\"${property("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    constants["release"] = property("mod.id") != "template"
}

tasks.register("runClientCurrentVersion") {
    group = "run"
    description = "Runs the client for the active Stonecutter version."
    dependsOn(project(":${sc.current?.version}").tasks.named("runClient"))
}

tasks.register("runServerCurrentVersion") {
    group = "run"
    description = "Runs the server for the active Stonecutter version."
    dependsOn(project(":${sc.current?.version}").tasks.named("runServer"))
}

val releaseVersions = listOf("1.21.11", "26.1.2")

tasks.register("buildReleaseRemapped") {
    group = "build"
    description = "Builds remapped jars for the release versions."
    dependsOn(releaseVersions.map { ":$it:buildAndCollectRemapped" })
}
