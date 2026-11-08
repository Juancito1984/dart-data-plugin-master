plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.20.0"
}

group = "com.nyk"
version = "1.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    intellijPlatform {
        androidStudio("2024.1.1.13")
        bundledPlugin("com.intellij.java")
        plugin("Dart", "241.18808")
        pluginVerifier()
    }
}

intellijPlatform {
    pluginConfiguration {
        name = "Dart Data Section NyK"
        ideaVersion {
            sinceBuild.set("241")
            untilBuild.set(provider { null })
        }
    }
}

tasks {
    named("buildSearchableOptions") {
        enabled = false
    }
    named("prepareJarSearchableOptions") {
        enabled = false
    }
    compileKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            allWarningsAsErrors.set(true)
        }
    }
    compileTestKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            allWarningsAsErrors.set(true)
        }
    }
}
