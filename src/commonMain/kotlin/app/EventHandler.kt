package app.ddd.app

import kotlin.reflect.KClass

abstract class EventHandler<E : Any>(val eventType: KClass<E>) {

    open suspend fun handle(event: E, message: EventMessage) {}

    open suspend fun handle(message: EventMessage) {
        @Suppress("UNCHECKED_CAST")
        handle(message.event as E, message)
    }
}