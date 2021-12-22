# Domain-driven design using Kotlin
This framework uses CQRS and ES patterns and is coded using [Multiplatform](https://kotlinlang.org/docs/multiplatform.html) Kotlin. It's inspired by [AxonFramework](https://github.com/AxonFramework/AxonFramework) and [EventFlow](https://github.com/eventflow/EventFlow). Domain logic can be at the client side and/or it can be used from the server.

## Features
- Supports all platforms (Android, iOS, JS, JVM, Linux, macOS, tvOS, watchOS, Wasm, Windows)
- Fast as possible
  - Fully reflectionless and `kotlin-reflect` not needed
  - Dependency injection at build time
  - Minifiable code and [Proguard](https://github.com/Guardsquare/proguard) support
  - Commands are handled simultaneously. Transaction locks only single aggregate by ID and there isn't global sequences.
  - Asyncronous event handling using [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
  - Fast reflectionless serialization
  - Storage size is minimal when used [Protocol Buffers](https://developers.google.com/protocol-buffers)
- Code generation with Gradle plugin
- [Ktor](https://ktor.io/) support
  - Server-side authorization
- [Kotest](https://kotest.io/) support

Currently this framework is at early development stage :(

## Learn by simple example
1. Write your bounded context code first. For example:
  ```kt
/** Sales context **/
package com.example.sales

// Value objects
data class OrderLine(val prod: UByte, val price: Float, val qty: UShort = 1u) {
    val empty get() = qty == UShort.MIN_VALUE
    val total get() = price * qty.toFloat()
}
data class VatNo(val countryCode: String, val no: Int) {
    init {
        if (countryCode.length != 2 || no.toString().length != 8)
            throw InvalidVatNo(this)
    }
    override fun toString() = "$countryCode-$no"
}

// Domain exceptions
data class BannedCustomer(val customer: VatNo) : Exception()
data class InvalidVatNo(val vatNo: VatNo) : Exception()

interface SalesBans : Set<VatNo> // Domain service

interface Events { // Domain events as interface methods
    fun quotationOffered(customer: VatNo, lines: List<OrderLine>)
}

/** Sales order aggregate root entity **/
class Order(val emit: Events) : Events {
    companion object { lateinit var bans: SalesBans }

    // Public accessors are safe to use and allows snapshotting
    lateinit var customer: VatNo
    var lines = listOf<OrderLine>()

    // Command handler for business logic
    fun requestForQuotation(customer: VatNo, lines: List<OrderLine>) {
        if (customer in bans) throw BannedCustomer(customer)
        emit.quotationOffered(customer, lines.filter { it.empty })
    }

    // Event handler to alter the state
    override fun quotationOffered(customer: VatNo, lines: List<OrderLine>) {
        this.customer = customer
        this.lines += lines
    }
}
  ```
2. Following application classes are generated based on your context code before build:
  - Aggregate
  - Aggregate factory
  - Aggregate root entity test stub
  - Application service (based on commands)
  - Domain command value objects
  - Domain configurer
  - Domain event value objects
  - Domain service test stub and possibly in-memory implementation
3. Now you can create unit tests for the context. Skipping this now for simplicity.
4. Implement your interfaces and create custom event handlers in the infrastructure layer:
  ```kt
// Domain service implementation
class SalesBansImpl(vararg ban: VatNo) : SalesBans, Set<VatNo> by ban.toSet()

object QuotationSender : EventHandler<QuotationOffered>(QuotationOffered::class) {
    override suspend fun handle(event: QuotationOffered, message: EventMessage) {
        // ...create PDF and send it via email...
    }
}
  ```
5. Test your implementations.
6. Build the application with Dukt libraries:
  - `dukt-app` Mandatory application base classes command and event processing
  - Separate package for each Event store implementation
7. Thats it! Now application is ready for testing and deploying.

## Planned later
- Saga support
- Code migration from/to other frameworks/languages
