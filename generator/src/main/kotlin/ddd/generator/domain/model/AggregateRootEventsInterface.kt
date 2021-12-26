package app.ddd.generator.domain.model

import app.oop.model.TypeDef

class AggregateRootEventsInterface(definition: TypeDef) : DomainObject(definition) {
    val events by lazy { block.methods.map { AggregateEvent(it) } }
}