package app.ddd.generator.documentation

import app.ddd.common.pluralize
import app.ddd.generator.application.model.Application
import app.ddd.generator.domain.model.*

class DocumentationGenerator(documentation: Documentation) : Documentation by documentation {
    fun documentApplication(application: Application) = with(application) {
        heading(label)
        definition("Domain package", config.domain)
        with(boundedContexts) {
            definition("Bounded contexts", "$size (${joinToString()})")
            horizontalRule()
            level { forEach(::documentBoundedContext) }
        }
    }

    private fun documentBoundedContext(boundedContext: BoundedContext) = with(boundedContext) {
        heading(label)
        definition("Module", module)
        aggregateRoot?.let { definition("Aggregate root", it) }
        definition("Other types", mapOf(
            "value object" to valueObjects,
            "exception" to exceptions,
            "service interface" to serviceInterfaces
        ).map { (type, list) -> type.pluralize(list) }.joinToString())
        level {
            aggregateRoot?.let {
                with(aggregateRoot) {
                    heading(label)
                    level {
                        events.forEach(::documentEvent)
                        exceptions.forEach(::documentException)
                        serviceInterfaces.forEach(::documentService)
                    }
                }
            }
            valueObjects.forEach(::documentValueObject)
        }
        horizontalRule()
    }

    private fun documentEvent(event: AggregateEvent) = with(event) {
        heading(label)
        ordered(properties)
    }

    private fun documentException(exception: DomainException) = with(exception) {
        heading(label)
    }

    private fun documentService(service: DomainServiceInterface) = with(service) {
        heading(label)
    }

    private fun documentValueObject(valueObject: ValueObject) = with(valueObject) {
        heading(label)
        ordered(properties)
    }
}
