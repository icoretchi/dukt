package app.ddd.app.impl

import app.ddd.app.Aggregate
import app.ddd.app.*

class EventStoreRepository<R : Any>(
    private val eventStore: EventStore
) : AggregateRepository<R>() {
    override suspend fun get(of: Of): Aggregate<R> {
        val aggregate = create(of)
        if (of.mayExists) {
            val messages = eventStore.get(of.id)
            messages.lastOrNull()?.let { last ->
                aggregate.batch += last.batch
                aggregate.sequence = last.sequence
                messages.forEach { message -> aggregate.apply(message.event) }
            }
        }
        return aggregate
    }
}
