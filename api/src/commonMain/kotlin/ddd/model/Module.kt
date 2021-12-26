package app.ddd.model

import app.oop.model.Packaged

data class Module(override val name: String, override val packageName: String) : Packaged