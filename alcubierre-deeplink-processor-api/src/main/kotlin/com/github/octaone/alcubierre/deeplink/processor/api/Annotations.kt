package com.github.octaone.alcubierre.deeplink.processor.api

/**
 * An annotation that allows you to specify a set of deeplinks that can be used to open the screen.
 * Only works with applied KSP processor!
 * ```
 * @Deeplink("myapp://feature/{id}")
 * @Deeplink("https://my.app/feature/{id}")
 * data class FeatureScreen(val id: String)
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Repeatable
public annotation class Deeplink(val pattern: String)

/**
 * An annotation that allows you to give a class field a custom name contained in deeplink placeholders.
 * ```
 * @Deeplink("myapp://feature/{ID}")
 * data class FeatureScreen(@DeeplinkParam(name = "ID") val id: String)
 * ```
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
public annotation class DeeplinkParam(val name: String)
