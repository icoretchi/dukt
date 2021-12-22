package add.ddd.plugin

import app.ddd.generator.domain.model.BoundedContext
import org.gradle.api.*

class DuktPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("Dukt applied for ${BoundedContext(project.name)}")
    }
}
