package app.oop.model

interface Packaged {
    /**
     * Simple names e.g. Map.Entry or FileName.Extension
     * Anonymous type has empty name.
     */
    val name: String

    /**
     * Package qualified name e.g. app.oop.model
     * Default package is empty string.
     */
    val packageName: String

    val qualifiedName get() = "$packageName.$name"
}
