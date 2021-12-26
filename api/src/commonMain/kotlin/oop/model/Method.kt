package app.oop.model

/**
 * Class method or function like a procedure. Function as a name conflicts with [kotlin.Function].
 */
interface Method : Packaged {
    /**
     * Parameters
     */
    val parameters: List<Variable>
}