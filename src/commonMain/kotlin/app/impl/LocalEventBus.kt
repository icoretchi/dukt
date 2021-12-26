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

    override suspend fun dispatch(messages: List<EventMessage>) {
        // Todo add async
        //coroutineScope {
            //val storeJob = launch {
                eventStore.add(messages)
            //}
            // Todo async handlers
            messages.map { message ->
                val messageJobs = getHandlers(message.event::class).map { handler ->
                    //launch {
                        handler.handle(message)
                    //}
                }
            }
        //}
    }

    private fun getHandlers(eventType: KClass<*>) = handlerRegistry.getOrPut(eventType) { mutableListOf() }

    override fun minus(handler: EventHandler<*>) = getHandlers(handler.eventType).remove(handler)

    override fun plus(handler: EventHandler<*>) = getHandlers(handler.eventType).add(handler)
}
