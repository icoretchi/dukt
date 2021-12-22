plugins {
    `java-gradle-plugin`
    kotlin("jvm")
}

dependencies {
    implementation(project(":generator"))
}

gradlePlugin {
    plugins {
        create("dukt") {
            id = "app.ddd.dukt"
            implementationClass = "app.ddd.plugin.DuktPlugin"
        }
    }
}
