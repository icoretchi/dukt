package app.oop.model.psi

import app.oop.model.CodeFile
import org.jetbrains.kotlin.cli.jvm.compiler.*
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.jvm.KotlinJavaPsiFacade
import java.io.File
import java.nio.file.Path

/**
 * [Parsing Kotlin code using Kotlin](https://jitinsharma.in/posts/parsing-kotlin-using-code-kotlin/)
 * [Program Structure Interface (PSI)](https://plugins.jetbrains.com/docs/intellij/psi.html)
 * TODO ignore generated files
 */
object KotlinPsi {
    private val project = KotlinCoreEnvironment.createForProduction(
        Disposer.newDisposable(),
        CompilerConfiguration(),
        EnvironmentConfigFiles.METADATA_CONFIG_FILES
    ).project

    val manager = PsiManager.getInstance(project)

    val facade = KotlinJavaPsiFacade.getInstance(project)

    operator fun invoke(path: Path) = invoke(path.toFile())

    operator fun invoke(file: File) = KotlinFile(manager.findFile(
        LightVirtualFile(file.name, KotlinFileType.INSTANCE, file.readText())) as KtFile) as CodeFile
}