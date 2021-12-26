package app.oop.model

/**
 * Source code file
 */
interface CodeFile : Packaged {
    val types: List<TypeDef>

    val path: String
}