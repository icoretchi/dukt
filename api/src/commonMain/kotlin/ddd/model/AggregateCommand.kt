package app.ddd.model

import app.oop.model.*

interface AggregateCommand : BuildingBlock {
    companion object {
        /**
         * Handler name prefix
         */
        const val PREFIX = "do"
    }

    val method: Method

    override val name get() = method.name.substringAfter(PREFIX)

    val properties: List<Variable>
}