package app.ddd.generator.application.model

import app.ddd.common.moduleToPath
import java.nio.file.Path
import kotlin.io.path.Path

data class Config(
    /**
     * Project group
     */
    var group: String,

    /**
     * Application domain module
     */
    var domain: String = "$group.domain",

    /**
     * Project relative source directory
     */
    var sources: Path = Path("src", "main", "kotlin"),

    /**
     * Working directory
     */
    var working: Path = Path(""),

    /**
     * Project relative domain directory
     * TODO detect common root package
     */
    var domainDir: Path = working.resolve(sources).resolve(domain.moduleToPath()),

    /**
     * Project relative generated documentation directory
     */
    var generatedDocumentation: Path = Path("build", "generated"),

    /**
     * Project relative generated main source directory
     */
    var generatedSources: Path = Path("build", "generated", "main"),

    /**
     * Project relative generated test source directory
     */
    var generatedTestSources: Path = Path("build", "generated", "test")
)
