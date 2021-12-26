package app.ddd.app.common.rule

data class Or<T>(private val left : Specification<T>, private val right: Specification<T>)
    : Specification<T> {
    override fun invoke(candidate: T) = left(candidate) || right(candidate)

    override fun toString() = "($left) or ($right)"
}