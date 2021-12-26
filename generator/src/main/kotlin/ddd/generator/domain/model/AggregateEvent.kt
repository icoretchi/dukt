package app.ddd.generator.domain.model

import app.oop.model.Method

class AggregateEvent(val method: Method) : BuildingBlock<Method>(method) {
    companion object {
        const val PREFIX = "on"
    }

    override val module = super.module.replace(".domain", ".app.domain")

    override val name by lazy { method.name.substringAfter(PREFIX) }

    val properties get() = block.parameters
}
