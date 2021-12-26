package app.oop.model

/**
 * Type definition
 */
interface TypeDef : Packaged {
    val extends get() = implements.firstOrNull()

    val file : CodeFile

    /**
     * All types which are implemented by this class. Super type is first if extended.
     */
    val implements: List<TypeRef>

    /**
     * Is data type
     */
    val isData: Boolean

    val isEnumeration: Boolean

    val isException get() = extends?.name?.endsWith("Exception") ?: false

    val isInterface: Boolean

    val methods: List<Method>

    /**
     * Primary constructor parameters
     */
    val primaryConstructor : List<Variable>

    val properties: List<Variable>
}