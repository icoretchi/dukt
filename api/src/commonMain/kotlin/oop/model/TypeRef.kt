package app.oop.model

/**
 * Type reference
 */
interface TypeRef : Packaged {
    /**
     * Generic type arguments
     */
    val argument get() = arguments.first()

    /**
     * Generic type arguments
     */
    val arguments: List<TypeRef>

    /**
     * File where this reference is used.
     */
    val file: CodeFile

    val isCollection get() = name.endsWith("Collection") || isList

    val isList get() = name.endsWith("List")

    /**
     * Simple names eg. Map.Entry
     */
    val simpleNames: List<String>
}