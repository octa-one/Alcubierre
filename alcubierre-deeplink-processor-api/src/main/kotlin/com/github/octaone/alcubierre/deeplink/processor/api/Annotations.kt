package com.github.octaone.alcubierre.deeplink.processor.api

/**
 * Аннотация, позволяющая указать набор диплинков, по которым можно открыть экран приложения.
 * ```
 * @Deeplink("myapp://feature/{id}")
 * @Deeplink("https://myapp.ru/app/feature/{id}")
 * data class FeatureScreen(val id: String)
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Repeatable
public annotation class Deeplink(val pattern: String)

/**
 * Аннотация, позволяющая задать полю класса кастомное название, содержащееся в плейсхолдере диплинка
 * пример использования
 * ```
 * @Deeplink("myapp://feature/{ID}")
 * data class FeatureScreen(@DeeplinkParam(name = "ID") val id: String)
 * ```
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
public annotation class DeeplinkParam(val name: String)
