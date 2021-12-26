package app.ddd.common

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class StringExtensionsTest : FunSpec({
    context("camelCaseToWords") {
        mapOf(
            "word" to "word",
            "camelCase" to "camel Case",
            "PascalCase" to "Pascal Case",
            "CAPS" to "CAPS",
            "NameIDResolver" to "Name ID Resolver"
        ).forEach {
            test(it.toString()) { -> it.key.camelCaseToWords() shouldBe it.value }
        }
    }
})
