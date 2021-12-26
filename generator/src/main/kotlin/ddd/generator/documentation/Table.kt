package app.ddd.generator.documentation

interface Table {
    fun build()

    fun column(header: Any, align: Align = Align.Left): Table

    fun columnCenter(header: Any) = column(header, Align.Center)

    fun columnLeft(header: Any) = column(header, Align.Left)

    fun columnRight(header: Any) = column(header, Align.Right)

    fun row(vararg texts: Any): Table
}