package app.ddd.common

/**
 * https://stackoverflow.com/questions/7593969/regex-to-split-camelcase-or-titlecase-advanced
 */
private val camelCaseRegex = "(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])".toRegex()

fun String.camelCaseToWords() = splitCamelCase().joinToString(" ")

fun String.splitCamelCase() = split(camelCaseRegex)

fun String.lowerFirst() = replaceFirstChar { it.lowercase() }

fun String.upperFirst() = replaceFirstChar { it.uppercase() }