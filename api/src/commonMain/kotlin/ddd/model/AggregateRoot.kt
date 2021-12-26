package app.ddd.model

interface AggregateRoot : DomainObject {
    val commands: List<AggregateCommand>

    val events get() = eventsInterface.events

    val eventsInterface: AggregateRootEventsInterface
}