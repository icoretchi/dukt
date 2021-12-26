package app.ddd.app

abstract class ApplicationService<R : Any>(private val factory: AggregateFactory<R>) {
    private val bus by lazy { EventBus.holder.get() }

    private val repository by lazy { AggregateRepository[factory] }

    protected suspend fun <T> tx(of: Of, action: (root: R) -> T): T {
        val aggregate = repository.get(of)
        val events: List<EventMessage>
        val result: T
        try {
            result = action(aggregate.root)
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