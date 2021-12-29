package app.ddd.app.impl

import app.ddd.app.*

class EventStoreRepository<R : Any, E : Any>(
    private val eventStore: EventStore
) : AggregateRepository<R, E>() {
    override suspend fun get(of: Of): Aggregate<R, E> {
        val aggregate = create(of)
        if (of.mayExists) {
            val messages = eventStore.get<E>(of.id)
            messages.lastOrNull()?.let { last ->
                aggregate.batch += last.batch
                aggregate.sequence = last.sequence
                messages.forEach { message -> aggregate.apply(message.event) }
            }
        }
        return aggregate
    }
}
