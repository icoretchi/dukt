package app.ddd.app

import app.ddd.app.common.missingService
import kotlin.reflect.KClass

abstract class AggregateRepository<R : Any, E : Any> {
    private lateinit var factory: AggregateFactory<R, E>

    protected fun create(of: Of) = factory.create(of)

    companion object {
        val registry = mutableMapOf<KClass<*>, AggregateRepository<*, *>>()

        inline operator fun <reified T : Any> plus(instance: AggregateRepository<T, *>) {
            registry[T::class] = instance
        }

        operator fun <TR : Any,TE : Any > get(factory: AggregateFactory<TR, TE>) : AggregateRepository<TR, TE> {
            @Suppress("UNCHECKED_CAST")
            val repository = registry[factory.rootType] as AggregateRepository<TR, TE>?
                ?: throw missingService("Repository<${factory.rootTypeName}>", factory.rootTypeName)
            repository.factory = factory
            return repository
        }
    }

    /**
     * Get aggregate and lock it until released.
     */
    abstract suspend fun get(of: Of): Aggregate<R, E>

    /**
     * Release aggregate lock and allow caching.
     */
    open operator fun plus(aggregate: Aggregate<R, E>) {}
}
