package app.ddd.app.impl

import app.ddd.app.*
import com.benasher44.uuid.Uuid

object InMemEventStore : EventStore {
    private val eventsById = mutableMapOf<Uuid, MutableList<EventMessage<*>>>()

    @Suppress("UNCHECKED_CAST")
    override suspend fun <E : Any> get(id: Uuid) = eventsById[id] as List<EventMessage<E>>? ?: emptyList()

    override suspend fun add(messages: List<EventMessage<*>>) {
        eventsById.getOrPut(messages.first().source) { mutableListOf() }.addAll(messages)
    }
}
