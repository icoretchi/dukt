package app.ddd.app

import kotlin.reflect.KClass

abstract class EventHandler<E : Any>(val eventType: KClass<E>) {

    open suspend fun handle(event: E, message: EventMessage<out E>) {}

    open suspend fun handle(message: EventMessage<E>) {
        handle(message.event, message)
    }
}
