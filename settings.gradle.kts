rootProject.name = "2024-10-otus-java-valter"
include("hw01-gradle")
include("hw02-generics")
include("hw03-annotations")
include("hw04-gc")
include("hw05-byteCodes")
include("hw06-solid")
include("hw07-structuralPatterns")
include("hw08-io")
include("hw09-jdbc")
include("hw10-hibernate")
include("hw11-cache")
include("hw12-WebServer")
include("hw13-di")
include("hw14-springDataJdbc")
include("hw15-executors")
include("hw16-concurrentCollections")
include("hw17-grpc")
include("hw18-webflux-chat:client-service")
include("hw18-webflux-chat:datastore-service")

pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}
