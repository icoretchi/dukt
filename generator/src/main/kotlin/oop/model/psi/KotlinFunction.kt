package app.oop.model.psi

import app.oop.model.Method
import org.jetbrains.kotlin.psi.KtNamedFunction

class KotlinFunction(element: KtNamedFunction, private val file: KotlinFile)
    : PackagedElement<KtNamedFunction>(element), Method {
    override val packageName get() = file.packageName

    override val parameters by lazy { element.valueParameters.map { KotlinVariable(file, it) } }
}