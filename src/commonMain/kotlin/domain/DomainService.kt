package app.ddd.domain

import app.ddd.app.common.missingService

/**
 * Service locator for domain dependencies.
 */
object DomainService {
    val registry = mutableListOf<Any>()

    inline fun <reified S> get() = (registry.find { it is S } as S?)
        ?: throw missingService(S::class.simpleName!!, "domain service")

    operator fun plus(service: Any) = registry.add(service)
}