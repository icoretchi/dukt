package app.ddd.gradle.plugin

import app.ddd.generator.application.model.*
import org.gradle.api.*
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.nio.file.Path
import java.util.*

class DuktPlugin : Plugin<Project> {
    private val app by lazy {
        // Group is null at apply phase
        val group = "${project.group}"
        if (group.isBlank()) throw Exception("Define project group!")

        Application(Config(
            group,
            sources = sourcePath,
            working = working,
            domainDir = sourcePath,
            generatedDocumentation = generated,
            generatedSources = generatedMain,
            generatedTestSources = generatedTest
        ))
    }
    lateinit var config: Config
    lateinit var generated: Path
    lateinit var generatedMain: Path
    lateinit var generatedTest: Path
    var multiplatform = false
    lateinit var project: Project
    private val props = javaClass.getResourceAsStream("/dukt.properties").use { i -> Properties().also { it.load(i) }}
    lateinit var sourcePath: Path
    lateinit var working: Path

    override fun apply(project: Project) {
        this.project = project
        with (project) {
            multiplatform = plugins.hasPlugin(MULTIPLATFORM)
            if (!multiplatform && setOf(ANDROID, JS, JVM).none { plugins.hasPlugin(it) })
                throw Exception("Apply Kotlin plugin before Dukt!")
            val mainName = if (multiplatform) KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME else SourceSet.MAIN_SOURCE_SET_NAME
            val testName = if (multiplatform) KotlinSourceSet.COMMON_TEST_SOURCE_SET_NAME else SourceSet.TEST_SOURCE_SET_NAME
            val sourceSets = extensions.getByType(KotlinProjectExtension::class.java).sourceSets
            val mainSourceSet = sourceSets.getByName(mainName)
            mainSourceSet.dependencies {
                implementation("app.ddd.dukt:dukt:${props["vDukt"]}")
            }
            val main = mainSourceSet.kotlin
            val test = sourceSets.getByName(testName).kotlin
            generated = buildDir.toPath().resolve("generated")
            generatedMain = generated.resolve(mainName)
            generatedTest = generated.resolve(testName)

            sourcePath = main.srcDirs.first().toPath()
            main.srcDir(generatedMain)
            test.srcDir(generatedTest)

            working = projectDir.toPath()
        }

        project.task("cleanGenerated") {
            doLast {
                generated.toFile().deleteRecursively()
            }
        }.description = "Clean generated files."

        project.task("generateContext") {
            doLast {
                app.generate()
            }
        }.description = "Generate support classes for bounded context."

        project.task("generateDocumentation") {
            doLast {
                app.document()
            }
        }.description = "Generate bounded context documentation."
    }
}
