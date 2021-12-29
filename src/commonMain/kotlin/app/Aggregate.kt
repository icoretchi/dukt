package app.ddd.app

abstract class Aggregate<R : Any, E : Any>(target: Of) {

    /**
     * Current transaction order number in this aggregate.
     */
    var batch = 1

    private val transaction = mutableListOf<EventMessage<E>>()

    val id = target.id

    lateinit var root: R

    private val rootName by lazy { root::class.simpleName!! }

    /**
     * Last emitted event sequence number.
     */
    var sequence = 0

    var user = target.user

    abstract fun apply(event: E)

    fun commit() : List<EventMessage<E>> {
        val events = transaction.toList()
        transaction.clear()
        ++batch
        return events
    }

    protected operator fun plus(event: E) {
        transaction.add(EventMessage(batch, event, event::class.simpleName!!, ++sequence, id, rootName, user))
    }

    fun rollback() {
        sequence -= transaction.size
        transaction.clear()
    }
}
