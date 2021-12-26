package app.oop.model.psi

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import java.io.PrintStream

fun PsiElement.println(stream: PrintStream = System.out, indent: String = "") {
    stream.println("$indent${this::class.simpleName}: $this '$text'")
    children.forEach { it.println(stream, "$indent  ") }
}