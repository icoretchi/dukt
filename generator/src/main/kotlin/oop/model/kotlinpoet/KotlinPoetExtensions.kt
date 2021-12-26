package app.oop.model.kotlinpoet

import app.ddd.common.lowerFirst
import app.oop.model.*
import com.squareup.kotlinpoet.*

val illegalArgumentType = ClassName("kotlin", "IllegalArgumentException")
val serializableType = ClassName("kotlinx.serialization", "Serializable")


val Array<out ClassName>.asParameters get() = map { ParameterSpec(it.simpleName.lowerFirst(), it) }

val Array<out Pair<String, TypeName>>.asParameters get() = map { ParameterSpec(it.first, it.second) }

fun FileSpec.print() {
    println()
    println("Printing file $name.kt:")
    println("-----")
    writeTo(System.out)
}

fun FileSpec.Builder.addTypes(types: Iterable<TypeSpec>): FileSpec.Builder {
    types.forEach { addType(it)}
    return this
}

//fun FileSpec.Builder.print() = build().print()

fun FunSpec.Companion.constructor(vararg types: ClassName) =
    constructorBuilder().addParameters(types.asParameters).build()

fun FunSpec.Companion.overrideBuilder(name: String, parameters: Iterable<ParameterSpec>) =
    builder(name).addModifiers(KModifier.OVERRIDE).addParameters(parameters)

fun FunSpec.Companion.overrideBuilder(name: String, parameters: List<Variable>) =
    overrideBuilder(name, parameters.asParameters)

fun FunSpec.Companion.overrideBuilder(name: String, vararg parameters: Pair<String, TypeName>) =
    overrideBuilder(name, parameters.asParameters)

fun FunSpec.Companion.overrideBuilder(name: String, returns: TypeName, vararg parameters: Pair<String, TypeName>) =
    overrideBuilder(name, parameters.asParameters).returns(returns)

val List<Variable>.asConstructor get() = FunSpec.constructorBuilder().let { builder ->
    forEach { builder.addParameter(it.name, it.asClassName) }
    builder.build()
}

/**
 * Variables as constructor initialized properties.
 */
val List<Variable>.asInitialized get() = map { it.asInitializedProperty }

val List<Variable>.asParameters get() = map { ParameterSpec(it.name, it.asClassName) }

fun Method.overrideBuilder() = FunSpec.overrideBuilder(name, parameters)

val TypeRef.asClassName get() = ClassName(packageName, simpleNames)

fun TypeSpec.Companion.classBuilder(type: ClassName, vararg params: ClassName) =
    classBuilder(type).primaryConstructor(FunSpec.constructor(*params))

/**
 * Get class builder with constructor.
 */
fun TypeSpec.Companion.classBuilder(name: String, vararg params: ClassName) =
    classBuilder(name).primaryConstructor(FunSpec.constructor(*params))

/**
 * Build object.
 */
//fun TypeSpec.Companion.singleton(name: String) = objectBuilder(name).build()

/**
 * Get data class builder with name and properties.
 */
fun TypeSpec.Companion.valueClassBuilder(name: String, properties: List<Variable>) =
    valueClassBuilder(name).primaryConstructorProperties(properties)

/**
 * Build data class with name and properties.
 */
//fun TypeSpec.Companion.valueClass(name: String, properties: List<Variable>) =
//    valueClassBuilder(name, properties).build()

fun TypeSpec.Builder.primaryConstructorProperties(properties: List<Variable>) =
    primaryConstructor(properties.asConstructor).addProperties(properties.asInitialized)

val Variable.asClassName get() = type.asClassName

/**
 * Variable as constructor initialized property.
 */
val Variable.asInitializedProperty get() = PropertySpec.builder(name, asClassName).initializer(name).build()

//val Variable.asProperty get() = PropertySpec.builder(name, asClassName).build()
