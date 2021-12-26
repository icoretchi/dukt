package app.ddd.model

import app.oop.model.*

interface DomainObject : BuildingBlock {
    val implements: List<TypeRef>

    override val label get() =
        "${super.label}${if (implements.isEmpty()) "" else " extends ${implements.joinToString()}"}"

    val methods: List<Method>

    val properties: List<Variable>

    val isCollection get() = implements.any { it.isCollection }
}