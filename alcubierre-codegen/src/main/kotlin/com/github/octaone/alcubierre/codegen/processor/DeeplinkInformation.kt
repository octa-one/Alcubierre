package com.github.octaone.alcubierre.codegen.processor

import com.github.octaone.alcubierre.codegen.api.Deeplink
import com.github.octaone.alcubierre.codegen.api.DeeplinkParam
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

/**
 * Служебная информация по классу, помеченному аннотацией [Deeplink].
 * Для одного класса может существовать несколько [DeeplinkInformation] c разными [patterns] и [placeholders].
 * Это происходит потому, что все [patterns], указанные в [Deeplink.patterns], группируются по набору плейсхолдеров.
 * Затем, на каждую группировку будет сгенерирован конвертер
 *
 * @property targetClass название класса, помеченного аннотацией [Deeplink]
 * @property screenConverter название и пакет [ScreenConverter], сгенерированного для шаблонов из [patterns]
 * @property constructorParams информация о параметрах конструктора [targetClass]
 */
data class DeeplinkInformation(
    val classDeclaration: KSClassDeclaration,
    val targetClass: ClassName,
    val screenConverter: ClassName,
    val patterns: List<String>,
    val placeholders: List<String>,
    val targetIsObject: Boolean,
    val constructorParams: List<ConstructorParameter>
)

/**
 * @property isMarkedNullable параметр имеет тип с вопросиком
 * @property hasDefault параметр имеет дефолтное значение
 *
 * @property name название параметра
 * @property placeholder название параметра в плейсхолдере,
 * может остутствовать, если одноименного плейсхолдера нет. Также, может отличаться от [name], если
 * над параметром указана аннотация [DeeplinkParam] с кастомным названием параметра
 *
 * @property className тип параметра
 * @property isEnum true, если тип параметра - enum
 */
data class ConstructorParameter(
    val isMarkedNullable: Boolean,
    val hasDefault: Boolean,
    val name: String,
    val placeholder: String?,
    val className: ClassName,
    val type: TypeName,
    val isEnum: Boolean
)