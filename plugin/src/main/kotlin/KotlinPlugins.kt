package app.ddd.gradle.plugin

/**
 * Kotlin plugin ID prefix
 */
private const val PREFIX = "org.jetbrains.kotlin"

/**
 * Kotlin Android platform plugin ID.
 * See: org.jetbrains.kotlin.gradle.plugin.KotlinPlatformAndroidPlugin
 */
const val ANDROID = "$PREFIX.android"

/**
 * Kotlin JS platform plugin ID.
 * See: org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJsPlugin
 */
const val JS = "$PREFIX.js"

/**
 * Kotlin JVM platform plugin ID.
 * See: org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
 */
const val JVM = "$PREFIX.jvm"

/**
 * Kotlin multiplatform plugin ID.
 * See: org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
 */
const val MULTIPLATFORM = "$PREFIX.multiplatform"
