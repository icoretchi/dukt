package app.ddd.app.impl

import app.ddd.app.EventMessage
import app.ddd.app.EventStore
import com.benasher44.uuid.Uuid

object InMemEventStore : EventStore {
    private val eventsById = mutableMapOf<Uuid, MutableList<EventMessage>>()

    override suspend fun get(id: Uuid) = eventsById[id] ?: emptyList()

    override suspend fun add(messages: List<EventMessage>) {
        eventsById.getOrPut(messages.first().source) { mutableListOf() }.addAll(messages)
    }
}