import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("plugin.serialization") version kotlinVersion
    kotlin("multiplatform") version kotlinVersion
    val joobyVersion: String by System.getProperties()
    id("io.jooby.run") version joobyVersion
}

version = "1.0.0-SNAPSHOT"
group = "com.example"

repositories {
    mavenCentral()
    mavenLocal()
}

// Versions
val kotlinVersion: String by System.getProperties()
val joobyVersion: String by System.getProperties()
val h2Version: String by project
val pgsqlVersion: String by project

val mainClassNameVal = "com.example.MainKt"

kotlin {
    jvmToolchain(17)
    jvm("backend") {
        withJava()
        compilations.all {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
            }
        }
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass.set(mainClassNameVal)
        }
    }
    js("frontend") {
        browser {
        }
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val backendMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
                implementation("io.jooby:jooby-kotlin:$joobyVersion")
                implementation("io.jooby:jooby-netty:$joobyVersion")
                implementation("io.jooby:jooby-guice:$joobyVersion")
                implementation("io.jooby:jooby-jackson:$joobyVersion")
                implementation("io.jooby:jooby-hikari:$joobyVersion")
                implementation("com.h2database:h2:$h2Version")
                implementation("org.postgresql:postgresql:$pgsqlVersion")
            }
        }
        val backendTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val frontendMain by getting {
            dependencies {
            }
        }
        val frontendTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

tasks {
    joobyRun {
        mainClass = mainClassNameVal
        restartExtensions = listOf("conf", "properties", "class")
        compileExtensions = listOf("java", "kt")
        port = 8080
    }
}
