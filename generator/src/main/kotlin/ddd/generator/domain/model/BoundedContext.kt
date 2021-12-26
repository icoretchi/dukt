package app.ddd.generator.domain.model

class BoundedContext(
    val aggregateRoot: AggregateRoot?,
    val exceptions: List<DomainException>,
    module: Module,
    val serviceInterfaces: List<DomainServiceInterface>,
    val valueObjects: List<ValueObject>,
    ) : BuildingBlock<Module>(module) {
    private val sharedKernel = name == "shared"

    override val displayName = if (sharedKernel) "Shared Kernel" else aggregateRoot?.displayName ?: name
}