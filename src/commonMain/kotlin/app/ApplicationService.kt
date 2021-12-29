package app.ddd.app

abstract class ApplicationService<R : Any, E : Any>(private val factory: AggregateFactory<R, E>) {
    private val bus by lazy { EventBus.holder.get() }

    private val repository by lazy { AggregateRepository[factory] }

    protected suspend fun <T> tx(of: Of, action: R.() -> T): T {
        val aggregate = repository.get(of)
        val events: List<EventMessage<E>>
        val result: T
        try {
            result = aggregate.root.action()
            events = aggregate.commit()
            repository + aggregate
        } catch (exception: Exception) {
            aggregate.rollback()
            throw DomainException(aggregate.id, exception)
        }
        bus.dispatch(events)
        return result
    }
}
