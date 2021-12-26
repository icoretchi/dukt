package app.ddd.generator.domain.model

import app.ddd.common.camelCaseToWords
import app.oop.model.Packaged

abstract class BuildingBlock<B : Packaged>(protected val block: B) {
    private val blockType by lazy { this::class.simpleName!!.camelCaseToWords().lowercase() }

    open val displayName by lazy { name.camelCaseToWords() }

    open val label by lazy { "$displayName $blockType" }

    open val module get() = block.packageName

    open val name get() = block.name

    override fun toString() = displayName
}
