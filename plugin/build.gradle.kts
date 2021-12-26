plugins {
    `java-gradle-plugin`

    /*
        WARNING: Unsupported Kotlin plugin version.
        The `embedded-kotlin` and `kotlin-dsl` plugins rely on features of Kotlin `1.5.31`
        that might work differently than in the requested version `1.6.10`.

        Gradle bundled Kotlin is old, but that doesn't cause problems. Follow issues:
        - https://github.com/gradle/gradle/issues/16345
        - https://github.com/gradle/gradle/issues/16779
     */
    `kotlin-dsl`

    `maven-publish`
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation(project(":generator"))
}

gradlePlugin {
    plugins {
        create("dukt") {
            id = "app.ddd.dukt"
            implementationClass = "app.ddd.gradle.plugin.DuktPlugin"
        }
    }
}

tasks {
    processResources {
        val props = file("$buildDir/generated/dukt.properties")

        doFirst {
            props.parentFile.mkdirs()
            props.writeText("vDukt=$version")
        }

        from(props)
    }
}
