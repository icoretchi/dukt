plugins {
    kotlin("multiplatform") version "1.6.10"
    id("app.ddd.dukt") version "0.1.0-SNAPSHOT"
    kotlin("plugin.serialization") version "1.6.10"
}

group = "com.corex.erp"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm()
}

tasks.wrapper {
    gradleVersion = "7.3.2"
}
