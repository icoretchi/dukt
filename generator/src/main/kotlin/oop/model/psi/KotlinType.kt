package app.oop.model.psi

import app.oop.model.TypeRef
import org.jetbrains.kotlin.psi.*

class KotlinType(override val file: KotlinFile, private val type: KtUserType) : TypeRef {

    constructor(file: KotlinFile, reference: KtTypeReference) : this(file, reference.typeElement as KtUserType)

    /**
     * Generic type arguments
     */
    override val arguments by lazy { type.typeArgumentsAsTypes.map { KotlinType(file, it) } }

    private val label by lazy { "$name${if (arguments.isEmpty()) "" else "<${arguments.joinToString()}>"}" }

    override val name get() = type.referencedName!!

    override val packageName by lazy { file.getPackageNameFor(name) }

    override val simpleNames = name.split('.')

    override fun toString() = label
}