package app.oop.model.psi

import app.oop.model.Variable
import org.jetbrains.kotlin.psi.*

class KotlinVariable(private val file: KotlinFile, element: KtNamedDeclaration, typeReference: KtTypeReference)
    : PackagedElement<KtNamedDeclaration>(element), Variable {

    constructor(file: KotlinFile, parameter: KtParameter) : this(file, parameter, parameter.typeReference!!)

    constructor(file: KotlinFile, property: KtProperty) : this(file, property, property.typeReference!!)

    override val packageName get() = file.packageName

    override val type = KotlinType(file, typeReference)

    override fun toString() = "$name: $type"
}