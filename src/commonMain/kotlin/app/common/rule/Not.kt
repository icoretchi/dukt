package app.ddd.app.common.rule

data class Not<T>(private val specification : Specification<T>) : Specification<T> {
    override fun invoke(candidate: T) = !specification(candidate)

    override fun toString() = "not $specification"
}