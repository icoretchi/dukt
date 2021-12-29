package app.ddd.app.impl

import app.ddd.app.*
import com.benasher44.uuid.Uuid

/**
 * TODO Convert to caching repository
 */
@Deprecated("Use EventStoreRepository with InMemEventStore instead.")
open class InMemRepository<R : Any, E : Any> : AggregateRepository<R, E>() {
    private val cache = mutableMapOf<Uuid, Aggregate<R, E>>()

    override suspend fun get(of: Of): Aggregate<R, E> {
        if (of.new) return create(of)
        var aggregate = cache[of.id]
        if (aggregate == null) {
            aggregate = create(of)
        } else {
            aggregate.user = of.user
        }
        return aggregate
    }

    override fun plus(aggregate: Aggregate<R, E>) {
        cache[aggregate.id] = aggregate
    }
}
