package app.ddd.generator.domain

import app.ddd.common.pluralize
import app.ddd.generator.domain.model.*
import app.oop.model.TypeDef
import app.oop.model.psi.KotlinPsi
import mu.KotlinLogging
import java.nio.file.Path
import kotlin.io.path.*

object DomainParser {
    private val log = KotlinLogging.logger {}

    private fun detected(list: List<*>, name: String) {
        if (list.any()) log.info { "Detected ${name.pluralize(list)}." }
    }

    operator fun invoke(domain: String, path: Path) : BoundedContext {
        val name = path.name
        val entries = path.listDirectoryEntries()
        log.debug { "Parsing ${"file".pluralize(entries.size)} from $path bounded context directory." }
        val files = entries.mapNotNull {
            log.debug { "Parsing ${it.name} file." }
            when (it.extension) {
                "kt" -> KotlinPsi(it)
                else -> {
                    log.warn { "Unsupported file extension ${it.extension} at $it!" }
                    null
                }
            }
        }
        log.debug {
            "Parsed ${files.size} of ${"code file".pluralize(entries.size)} from $name bounded context."
        }
        val types = files.flatMap { it.types }
        log.debug { "Detecting building blocks from ${"type".pluralize(types)}." }
        val exceptions = mutableListOf<DomainException>()
        val interfaces = mutableMapOf<String, TypeDef>()
        val rootCandidates = mutableListOf<TypeDef>()
        val valueObjects = mutableListOf<ValueObject>()
        types.forEach {
            when {
                it.isInterface -> interfaces[it.name] = it // TODO map using qualifiedName key
                it.isException -> exceptions.add(DomainException(it)) // May be data type or singleton.
                it.isData || it.isEnumeration -> valueObjects.add(ValueObject(it))
                it.primaryConstructor.any() -> rootCandidates.add(it)
                else -> log.warn { "$it type isn't detected!" }
            }
        }
        val roots = rootCandidates.filter { interfaces.containsKey(it.primaryConstructor.first().type.name) }
        if (roots.size > 1) log.warn { "Multiple aggregate roots found (${roots.joinToString()}) using first!" }
        val aggregateRoot = roots.firstOrNull()?.let {
            val root = AggregateRoot(AggregateRootEventsInterface(
                interfaces.remove(it.primaryConstructor.first().type.name)!!), it)
            log.info { "Detected $root aggregate root." }
            root
        }
        val serviceInterfaces = interfaces.values.map { DomainServiceInterface(it) }
        detected(valueObjects, "value object")
        detected(exceptions, "exception")
        detected(serviceInterfaces, "service interface")
        val module = Module(name, "$domain.$name")
        val context = BoundedContext(aggregateRoot, exceptions, module, serviceInterfaces, valueObjects)
        log.info {
            val detected = (if (aggregateRoot == null) 0 else 2) + exceptions.size + interfaces.size + valueObjects.size
            "Detected $detected of ${"type".pluralize(types.size)} in $context bounded context."
        }
        return context
    }
}