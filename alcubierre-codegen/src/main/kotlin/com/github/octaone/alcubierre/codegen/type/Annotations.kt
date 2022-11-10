package com.github.octaone.alcubierre.codegen.type

/**
 * Аннотация, позволяющая указать набор диплинков, по которым можно открыть экран приложения.
 * В аннотации можно указать несколько диплинков и апплинков.
 * ```
 * @Deeplink(
 *      "myapp://feature/{id}",
 *      "https://myapp.ru/app/feature/{id}"
 * )
 * data class FeatureScreen(val id: String)
 * ```
 */
@Target(AnnotationTarget.CLASS)
annotation class Deeplink(vararg val patterns: String)

/**
 * Аннотация, позволяющая задать полю класса кастомное название, содержащееся в плейсхолдере диплинка
 * пример использования
 * ```
 * @Deeplink("myapp://feature/{ID}")
 * data class FeatureScreen(@DeeplinkParam(name = "ID") val id: String)
 * ```
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class DeeplinkParam(val name: String)
