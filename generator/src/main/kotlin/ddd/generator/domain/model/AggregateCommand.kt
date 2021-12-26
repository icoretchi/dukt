package app.ddd.generator.domain.model

import app.oop.model.Method

class AggregateCommand(val method: Method) : BuildingBlock<Method>(method) {
    companion object {
        const val PREFIX = "do"
    }

    override val name by lazy { method.name.substringAfter(PREFIX) }

    val properties get() = block.parameters
}