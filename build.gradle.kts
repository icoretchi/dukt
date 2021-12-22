plugins {
    kotlin("multiplatform") version "1.6.10"
    //kotlin("plugin.serialization") version "1.6.10"
}

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
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.3.2"
}
