package app.ddd.model

interface BoundedContext : BuildingBlock {
    val aggregateRoot: AggregateRoot?

    val exceptions: List<DomainException>

    val serviceInterfaces: List<DomainServiceInterface>

    val valueObjects: List<ValueObject>

    val sharedKernel get() = name == "shared"

    override val displayName get() = if (sharedKernel) "Shared Kernel" else aggregateRoot?.displayName ?: name
}