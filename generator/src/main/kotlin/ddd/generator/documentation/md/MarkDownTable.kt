package app.ddd.generator.documentation.md

import app.ddd.generator.documentation.Align
import app.ddd.generator.documentation.Table

class MarkDownTable(private val out: Appendable) : Table {
    override fun build() {
        TODO("Not yet implemented")
    }

    override fun column(header: Any, align: Align): Table {
        TODO("Not yet implemented")
    }

    override fun row(vararg texts: Any): Table {
        TODO("Not yet implemented")
    }
}