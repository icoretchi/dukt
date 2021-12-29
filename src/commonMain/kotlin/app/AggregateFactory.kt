package app.ddd.app

import kotlin.reflect.KClass

abstract class AggregateFactory<R : Any, E : Any>(val rootType: KClass<R>) {
    val rootTypeName = rootType.simpleName!!

    abstract fun create(of: Of): Aggregate<R, E>
}
