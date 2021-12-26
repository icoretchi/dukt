package app.oop.model.psi

import app.oop.model.Packaged
import org.jetbrains.kotlin.com.intellij.psi.PsiNamedElement

abstract class PackagedElement<E : PsiNamedElement>(protected val element: E) : Packaged {
    override val name get() = element.name ?: ""

    override fun toString() = qualifiedName
}