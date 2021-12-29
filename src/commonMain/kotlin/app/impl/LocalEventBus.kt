package app.ddd.app.impl

import app.ddd.app.EventBus
import app.ddd.app.EventHandler
import app.ddd.app.EventMessage
import app.ddd.app.EventStore
//import kotlin.coroutines.coroutineScope
//import kotlin.coroutines.launch
import kotlin.reflect.KClass

/**
 * Default event bus implementation which dispatches events only in local instance.
 */
class LocalEventBus(private val eventStore: EventStore) : EventBus {
    private val handlerRegistry = mutableMapOf<KClass<*>, MutableList<EventHandler<*>>>()

    @Suppress("UNCHECKED_CAST")
    override suspend fun <E : Any> dispatch(messages: List<EventMessage<E>>) {
        // Todo add async
        //coroutineScope {
            //val storeJob = launch {
                eventStore.add(messages)
            //}
            // Todo async handlers
            messages.map { message ->
                val messageJobs = getHandlers(message.event::class).map { handler ->
                    //launch {
                        (handler as EventHandler<E>).handle(message)
                    //}
                }
            }
        //}
    }

    @Suppress("UNCHECKED_CAST")
    private fun <E : Any> getHandlers(eventType: KClass<E>) =
        handlerRegistry.getOrPut(eventType) { mutableListOf() } as MutableList<EventHandler<E>>

    override fun <E : Any> minus(handler: EventHandler<E>) = getHandlers(handler.eventType).remove(handler)

    override fun <E : Any> plus(handler: EventHandler<E>) = getHandlers(handler.eventType).add(handler)
}
