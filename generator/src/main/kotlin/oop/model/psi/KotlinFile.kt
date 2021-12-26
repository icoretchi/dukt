package app.oop.model.psi

import app.oop.model.CodeFile
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType

class KotlinFile(element: KtFile) : PackagedElement<KtFile>(element), CodeFile {
    /**
     * Qualified import names by alias name
     *
     * TODO Handle alias and multiple simple names
     */
    val imports by lazy {
        element.importDirectives.associate { import -> import.importedFqName!!.asString().let {
            it.substringAfterLast('.') to it.substringBeforeLast('.')
        } }
    }

    override val path: String get() = element.virtualFilePath

    override val packageName by lazy { element.packageDirective?.qualifiedName ?: "" }

    override val types by lazy { element.getChildrenOfType<KtClass>().map { KotlinClass(this, it) } }

    fun getPackageNameFor(name: String) = imports.getValue(name)
}