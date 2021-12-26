package app.ddd.generator.application.model

import app.ddd.common.listDirectories
import app.ddd.common.upperFirst
import app.ddd.generator.code.kotlinpoet.KotlinPoetGenerator
import app.ddd.generator.documentation.*
import app.ddd.generator.documentation.md.MarkDownDocumentation
import app.ddd.generator.domain.DomainParser
import kotlin.io.path.createDirectories

class Application(val config: Config) {
    /**
     * Bounded contexts in domain directory.
     */
    val boundedContexts by lazy { config.domainDir.listDirectories().map { DomainParser(config.domain, it) } }

    val label get() = "$name application"

    val name get() = config.group.substringAfterLast('.').upperFirst()

    val module = "${config.group}.app"

    val documentation by lazy {
        config.generatedDocumentation.createDirectories()
        config.generatedDocumentation.resolve("$name.md").toFile()
    }

    fun document() = documentation.bufferedWriter().use { document(MarkDownDocumentation(it)) }

    fun document(documentation: Documentation) =
        DocumentationGenerator(documentation).documentApplication(this)

    fun generate() = KotlinPoetGenerator(this).generate()
}
