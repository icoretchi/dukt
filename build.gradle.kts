plugins {
    `maven-publish`
    kotlin("multiplatform") version "1.6.10"
    //kotlin("plugin.serialization") version "1.6.10"
}

val vKotest: String by project

allprojects {
    group = "app.ddd.dukt"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

kotlin {
    js {
        nodejs()
    }
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("com.benasher44:uuid:0.3.1")
                setOf("datetime:0.3.1", "serialization-core:1.3.2").map {
                    api("org.jetbrains.kotlinx:kotlinx-$it")
                }
            }
        }
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.3.2"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
        }
    }
}

project("api") {
    apply<MavenPublishPlugin>()
    apply<org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper>()
    kotlin {
        jvm {
            testRuns["test"].executionTask.configure {
                useJUnitPlatform()
            }
        }

        sourceSets {
            val commonTest by getting {
                dependencies {
                    setOf("assertions-core", "framework-api", "runner-junit5").map {
                        implementation("io.kotest:kotest-$it:$vKotest")
                    }
                }
            }
        }
    }
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["kotlin"])
            }
        }
    }
}
