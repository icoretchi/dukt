package app.ddd.app

import app.ddd.app.internal.Holder

/**
 * Event bus
 */
interface EventBus {
    companion object {
        val holder = Holder(EventBus::class)
    }

    /**
     * Dispatch events emitted from single aggregate
     */
    suspend fun dispatch(messages: List<EventMessage>)

    operator fun minus(handler: EventHandler<*>): Boolean

    operator fun plus(handler: EventHandler<*>): Boolean
}