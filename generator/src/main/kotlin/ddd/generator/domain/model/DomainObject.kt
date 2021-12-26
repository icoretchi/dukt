package app.ddd.generator.domain.model

import app.oop.model.TypeDef
import app.oop.model.psi.KotlinClass

abstract class DomainObject(definition: TypeDef) : BuildingBlock<TypeDef>(definition) {
    val implements get() = block.implements

    override val label by lazy {
        "${super.label}${if (implements.isEmpty()) "" else " extends ${implements.joinToString()}"}"
    }

    val methods get() = block.methods

    val properties get() = block.properties

    val isCollection get() = implements.any { it.isCollection }

    fun makeSerializable() = (block as KotlinClass).makeSerializable()
}