package app.ddd.model

interface AggregateRootEventsInterface : DomainObject {
    val events: List<AggregateEvent>
}