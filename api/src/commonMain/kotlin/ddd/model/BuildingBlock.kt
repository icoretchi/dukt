package app.ddd.model

import app.ddd.common.camelCaseToWords

interface BuildingBlock {
    val blockType get() = this::class.simpleName!!.camelCaseToWords().lowercase()

    val displayName get () = name.camelCaseToWords()

    val label get() = "$displayName $blockType"

    val module: Module

    val name: String
}