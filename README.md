# Domain-driven design using Kotlin
This framework uses CQRS and ES patterns and is coded using Multiplatform Kotlin. It's inspired from [AxonFramework](https://github.com/AxonFramework/AxonFramework) and [EventFlow](https://github.com/eventflow/EventFlow).

## Features
- Supports all platforms (Android, iOS, JS, JVM, Linux, macOS, tvOS, watchOS, Wasm, Windows)
- Fast as possible
  - Fully reflectionless (`kotlin-reflect` not needed)
  - Minifiable code ([Proguard](https://github.com/Guardsquare/proguard) support)
  - Asyncronous (Kotlin Coroutines)
  - Dependency injection at build time
- Efficient serialization
  - Kotlin Serialization is fast
  - Storage size is minimal with ProtoBuf
- Code generation with Gradle plugin
- Ktor support (planned)

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
class Order(private val emit: Events) : Events {
    companion object { lateinit var bans: SalesBans }

    lateinit var customer: VatNo private set
    private val _lines = mutableListOf<OrderLine>()
    val lines get() = _lines.toList()

    // Command handler for business logic
    fun requestForQuotation(customer: VatNo, lines: List<OrderLine>) {
        if (customer in bans) throw BannedCustomer(customer)
        emit.quotationOffered(customer, lines.filter { it.empty })
    }

    // Event handler to alter the state
    override fun quotationOffered(customer: VatNo, lines: List<OrderLine>) {
        this.customer = customer
        _lines.addAll(lines)
    }
}
  ```
2. Additional code is generated based on your context code before build.
3. Now you can create unit tests for the context. Skipping this now for simplicity.
4. Implement your interfaces in infrastructure layer:
  ```kt
// Domain service implementation
class SalesBansImpl(vararg ban: VatNo) : SalesBans, Set<VatNo> by ban.toSet()
  ```
5. Test your implementations.
6. Build the application
7. Test it
