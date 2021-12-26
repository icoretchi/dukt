package app.ddd.model

import app.oop.model.*

interface AggregateEvent : BuildingBlock {
    companion object {
        /**
         * Handler name prefix
         */
        const val PREFIX = "on"
    }

    val method: Method

    override val name get() = method.name.substringAfter(AggregateCommand.PREFIX)

    val properties: List<Variable>
}