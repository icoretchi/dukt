package app.ddd.generator.documentation

interface Documentation {
    fun bold(text: Any) : String

    fun decrementLevel() : Documentation

    fun definition(term: Any, definition: Any) : Documentation

    fun heading(phrase: Any): Documentation

    fun heading(phrase: Any, level: Int): Documentation

    fun heading1(phrase: Any) = heading(phrase, 1)

    fun heading2(phrase: Any) = heading(phrase, 2)

    fun heading3(phrase: Any) = heading(phrase, 3)

    fun heading4(phrase: Any) = heading(phrase, 4)

    fun heading5(phrase: Any) = heading(phrase, 5)

    fun heading6(phrase: Any) = heading(phrase, 6)

    fun horizontalRule() : Documentation

    fun incrementLevel() : Documentation

    fun italic(text: Any) : String

    fun level(action: () -> Unit) {
        incrementLevel()
        action()
        decrementLevel()
    }

    fun ordered(list: List<Any>) : Documentation

    fun paragraph(lines: Any) : Documentation

    fun table() : Table
}