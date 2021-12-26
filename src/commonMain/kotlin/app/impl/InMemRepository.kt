package app.ddd.app.impl

import app.ddd.app.Aggregate
import app.ddd.app.Of
import app.ddd.app.AggregateRepository
import com.benasher44.uuid.Uuid

/**
 * TODO Convert to caching repository
 */
@Deprecated("Use EventStoreRepository with InMemEventStore instead.")
open class InMemRepository<R : Any> : AggregateRepository<R>() {
    private val cache = mutableMapOf<Uuid, Aggregate<R>>()

    override suspend fun get(of: Of): Aggregate<R> {
        if (of.new) return create(of)
        var aggregate = cache[of.id]
        if (aggregate == null) {
            aggregate = create(of)
        } else {
            aggregate.user = of.user
        }
        return aggregate
    }

    override fun plus(aggregate: Aggregate<R>) {
        cache[aggregate.id] = aggregate
    }
}
