package app.ddd.common

fun String.pluralize(collection: Collection<Any?> = emptyList()) =
    if (collection.isEmpty()) pluralize(0)
    else "${pluralize(collection.size)} (${collection.joinToString()})"

fun String.pluralize(count: Int = 1) = when {
    count == 1 -> "1 $this"
    endsWith('s') -> "$count ${this}es"
    else -> "$count ${this}s"
}
