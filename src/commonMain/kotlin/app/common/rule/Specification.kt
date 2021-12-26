package app.ddd.app.common.rule

/**
 * Business rule specification
 *
 * See: [Specification pattern](https://en.wikipedia.org/wiki/Specification_pattern)
 */
@Deprecated("Probably useless structure in Kotlin")
interface Specification<T> {
    fun and(other: Specification<T>) = And(this, other)

    fun andNot(other: Specification<T>) = And(this, Not(other))

    /**
     * Is this rule satisfied by given candidate
     */
    operator fun invoke(candidate: T): Boolean

    fun or(other: Specification<T>) = Or(this, other)

    fun orNot(other: Specification<T>) = Or(this, Not(other))

    fun not() = Not(this)
}