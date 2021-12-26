package app.ddd.generator.code.kotlinpoet

import app.ddd.common.lowerFirst
import app.ddd.generator.application.model.Application
import app.ddd.generator.domain.model.*
import app.oop.model.joinNames
import app.oop.model.kotlinpoet.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

private const val APP_PACKAGE = "app.ddd.app"
private const val APPLICATION = "Application"
private const val AGGREGATE = "Aggregate"
private const val AGGREGATE_FACTORY = "${AGGREGATE}Factory"
private const val AGGREGATE_REPOSITORY = "${AGGREGATE}Repository"
private const val IN_MEMORY = "InMem"
private const val SERVICE = "Service"
private val aggregateType = ClassName(APP_PACKAGE, AGGREGATE)
private val aggregateFactoryType = ClassName(APP_PACKAGE, AGGREGATE_FACTORY)
private val aggregateRepositoryType = ClassName(APP_PACKAGE, AGGREGATE_REPOSITORY)
private val applicationServiceType = ClassName(APP_PACKAGE, "ApplicationService")
private val eventStoreRepositoryType = ClassName("$APP_PACKAGE.impl", "EventStoreRepository")
private val domainServiceType = ClassName("app.ddd.domain", "DomainService")
private val ofType = ClassName(APP_PACKAGE, "Of")

fun Application.asTestType(domainServices: List<ClassName>): TypeSpec {
    val initDomainServices = FunSpec.overrideBuilder("initDomainServices")
        .addKdoc("Override this test method to initialize and register domain service with real implementations.")
    domainServices.forEach {
        initDomainServices.addStatement("%T + %T", domainServiceType, it)
    }
    val initRepositories = FunSpec.overrideBuilder("initRepositories")
        .addKdoc("Initializes and registers aggregate repositories.")
    boundedContexts.mapNotNull { it.aggregateRoot }.forEach {
        initRepositories.addStatement("%T + %T(eventStore)",
            aggregateRepositoryType,
            eventStoreRepositoryType.parameterizedBy(it.asClassName))
    }
    return TypeSpec.classBuilder(name + "Test" + APPLICATION)
        .addModifiers(KModifier.OPEN)
        .superclass(ClassName(APP_PACKAGE, APPLICATION))
        .addFunction(initDomainServices.build())
        .addFunction(FunSpec.overrideBuilder("initEventHandlers")
            .addKdoc("Override this test method to initialize event handlers and add them to event bus.")
            .build())
        .addFunction(initRepositories.build())
        .build()
}

val AggregateEvent.asType get() =
    (if (properties.isEmpty()) TypeSpec.objectBuilder(name).addAnnotation(serializableType)
    else TypeSpec.valueClassBuilder(name, properties).addAnnotation(serializableType)).build()

val AggregateRoot.asAggregateClassName get() = ClassName(appModule, name + AGGREGATE)

val AggregateRoot.asAggregateFactoryClassName get() = ClassName(appModule, name + AGGREGATE_FACTORY)

val AggregateRoot.asAggregateType get(): TypeSpec {
    val apply = FunSpec.overrideBuilder("apply", "event" to ANY)
        .beginControlFlow("when(event)")
    events.forEach { event -> apply.addStatement(
        "is %T -> root.${event.method.name}(${event.properties.joinToString { "event.${it.name}" }})", event.asClassName
    ) }
    apply.addStatement("""else -> %M(event, "event")""",
        MemberName("app.ddd.app.common", "throwUnsupported"))
        .endControlFlow()
    return TypeSpec.classBuilder(asAggregateClassName, ofType)
        .superclass(aggregateType.parameterizedBy(asClassName))
        .addSuperclassConstructorParameter("of")
        .addSuperinterface(eventsInterface.asClassName)
        .addFunction(apply.build())
        .addFunctions(events.map {
            with(it) {
                val params = "(${properties.joinNames()})"
                val constructor = if (properties.isEmpty()) "" else params
                method.overrideBuilder()
                    .addStatement("root.${method.name}$params")
                    .addStatement("this + %T$constructor", asClassName)
                    .build()
            }
        })
        .build()
}

val AggregateRoot.asAggregateFactoryType get() = TypeSpec.objectBuilder(asAggregateFactoryClassName)
    .superclass(aggregateFactoryType.parameterizedBy(asClassName))
    .addSuperclassConstructorParameter("%T::class", asClassName)
    .addFunction(FunSpec.overrideBuilder("create", asAggregateClassName, "of" to ofType)
        .addStatement("return %T(of).also { it.root = %T(it) }", asAggregateClassName, asClassName)
        .build())
    .build()

val AggregateRoot.asServiceType get() = TypeSpec.objectBuilder(name + SERVICE)
    .superclass(applicationServiceType.parameterizedBy(asClassName))
    .addSuperclassConstructorParameter("%T", asAggregateFactoryClassName)
    .addFunctions(commands.map {
        with(it) {
            FunSpec.builder(name.lowerFirst())
                .addModifiers(KModifier.SUSPEND)
                .addParameter("of", ofType)
                .addParameters(properties.asParameters)
                .addStatement("tx(of) { it.${method.name}(${properties.joinNames()}) }")
                .build()
        }
    })
    .build()

val AggregateRoot.asEventTypes get() = events.map { it.asType }

fun BoundedContext.file(name: String, action: (FileSpec.Builder) -> Unit): FileSpec {
    val builder = fileBuilder(name)
    action(builder)
    return builder.build()
}

fun BoundedContext.file(type: TypeSpec) = FileSpec.get(appModule, type)

fun BoundedContext.file(name: String, types: Iterable<TypeSpec>) = fileBuilder(name).addTypes(types).build()

fun BoundedContext.fileBuilder(name: String) = FileSpec.builder(appModule, name)

val BuildingBlock<*>.appModule get() = module.replace("domain", "app.domain")

val BuildingBlock<*>.asClassName get() = ClassName(module, name)

val DomainServiceInterface.asMutableListType get() = TypeSpec.objectBuilder(name + IN_MEMORY)
    .addSuperinterface(MUTABLE_LIST.parameterizedBy(implements.first().argument.asClassName),
        CodeBlock.of("mutableListOf()")).addSuperinterface(asClassName).build()
