package app.ddd.app

import app.ddd.app.internal.Holder
import com.benasher44.uuid.Uuid

/**
 * Store for all events
 */
interface EventStore {
    companion object {
        val holder = Holder(EventStore::class)
    }

    /**
     * Get all aggregate event messages
     */
    suspend fun <E : Any> get(id: Uuid): List<EventMessage<E>>

    /**
     * Store at least one event emitted from single aggregate
     */
    suspend fun add(messages: List<EventMessage<*>>)
}
