plugins {
    kotlin("jvm")
    `maven-publish`
}

val vKotest: String by project

dependencies {
    implementation(project(":api"))
    implementation("com.squareup:kotlinpoet:1.10.2")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
    implementation(kotlin("compiler-embeddable"))
    implementation("org.slf4j:slf4j-simple:1.7.32")

    setOf("assertions-core", "framework-api").map {
        testImplementation("io.kotest:kotest-$it:$vKotest")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
        }
    }
}
