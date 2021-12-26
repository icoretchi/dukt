package app.ddd.generator.code.kotlinpoet

import app.ddd.generator.application.model.Application
import app.ddd.generator.code.SourceCodeGenerator
import app.ddd.generator.domain.model.*
import com.squareup.kotlinpoet.*
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

class KotlinPoetGenerator(private val application: Application) : SourceCodeGenerator {
    /**
     * Domain service implementations
     */
    private val domainServices = mutableListOf<ClassName>()

    private val main = application.config.generatedSources
    private val test = application.config.generatedTestSources

    override fun generate(): Unit = with(application) {
        boundedContexts.forEach(::generate)
        FileSpec.get(module, asTestType(domainServices)).write(test)
    }

    private fun generate(boundedContext: BoundedContext) {
        boundedContext.valueObjects.forEach {
            it.makeSerializable()
        }
        boundedContext.aggregateRoot?.let {
            with(it) {
                boundedContext.file( "${name}Events", asEventTypes).write()
                boundedContext.file(asAggregateType).write()
                boundedContext.file(asAggregateFactoryType).write()
                boundedContext.file(asServiceType).write()
            }
        }
        boundedContext.serviceInterfaces.forEach {
            if (it.isCollection) {
                val type = it.asMutableListType
                boundedContext.file(type).write()
                domainServices.add(ClassName(it.appModule, type.name!!))
            }
        }
    }

    private fun FileSpec.write(output: Path = main) {
        var outputDirectory = output
        if (packageName.isNotEmpty()) {
            for (packageComponent in packageName.split('.').dropLastWhile { it.isEmpty() }) {
                outputDirectory = outputDirectory.resolve(packageComponent)
            }
        }

        Files.createDirectories(outputDirectory)

        val outputPath = outputDirectory.resolve("$name.kt")
        Sanitizer(OutputStreamWriter(
            Files.newOutputStream(outputPath),
            StandardCharsets.UTF_8
        )).use { writeTo(it) }
    }
}
