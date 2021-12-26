package app.ddd.generator.domain.model

import app.oop.model.TypeDef

class AggregateRoot(val eventsInterface: AggregateRootEventsInterface, root: TypeDef) : DomainObject(root) {
    val commands by lazy { methods.filter { it.name.startsWith(AggregateCommand.PREFIX) }.map { AggregateCommand(it) } }

    val events get() = eventsInterface.events
}