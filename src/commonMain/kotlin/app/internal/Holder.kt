package app.ddd.app.internal

import app.ddd.app.common.missingService
import kotlin.reflect.KClass

/**
 * Singleton holder that is lazily initialized
 */
class Holder<S : Any>(private val type: KClass<S>) {
    private var instance: S? = null

    fun set(value: S) {
        instance = value
    }

    fun get() = instance ?: throw missingService(type.simpleName!!)
}