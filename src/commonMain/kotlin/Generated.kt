package app.ddd

/**
 * Marks that class code is generated and therefore not to be modified.
 */
@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class Generated
