package app.oop.model.psi

import app.oop.model.TypeDef
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtConstructorCalleeExpression
import org.jetbrains.kotlin.psi.stubs.KotlinAnnotationEntryStub

class KotlinClass(override val file: KotlinFile, element: KtClass) : PackagedElement<KtClass>(element), TypeDef {

    override val implements by lazy {
        element.superTypeListEntries.map { KotlinType(file, it.typeReference!!) }
    }

    override val isData get() = element.isData()

    override val isEnumeration get() = element.isEnum()

    override val isInterface get() = element.isInterface()

    override val methods by lazy { element.body?.functions?.map { KotlinFunction(it, file) } ?: emptyList() }

    override val packageName get() = file.packageName

    override val primaryConstructor by lazy { element.primaryConstructorParameters.map { KotlinVariable(file, it) } }

    override val properties by lazy {
        if (element.isData()) primaryConstructor
        else TODO()
    }

    // TODO
    fun makeSerializable() {
        /*KotlinPsi.manager
        KotlinPsi.facade
        var any: Any = 1
        val foo = any as KtConstructorCalleeExpression
        val bar = any as AnnotationDescriptor
        val stub = any as KotlinAnnotationEntryStub
        if (!element.annotations.any { it.name!!.contains("Serializable") }) {
            element.addAnnotationEntry(
                KtAnnotationEntry(stub)
            )
        }*/
    }
}
