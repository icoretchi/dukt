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
    suspend fun <E : Any> dispatch(messages: List<EventMessage<E>>)

    operator fun <E : Any> minus(handler: EventHandler<E>): Boolean

    operator fun <E : Any> plus(handler: EventHandler<E>): Boolean
}
