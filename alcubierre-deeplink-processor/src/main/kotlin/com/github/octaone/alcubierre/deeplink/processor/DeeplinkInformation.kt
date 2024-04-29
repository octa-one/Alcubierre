package com.github.octaone.alcubierre.deeplink.processor

import com.github.octaone.alcubierre.deeplink.processor.api.Deeplink
import com.github.octaone.alcubierre.deeplink.processor.api.DeeplinkParam
import com.github.octaone.alcubierre.deeplink.processor.api.ScreenConverter
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

/**
 * Information for a class labeled with [Deeplink] annotation.
 * There may be multiple [DeeplinkInformation] with different [patterns] and [placeholders] for the same class.
 * This is because all [patterns] specified in [Deeplink.pattern] are grouped by a set of placeholders.
 * Then, a converter will be generated for each group.
 *
 * @property targetClass The name of the class labeled with the [Deeplink] annotation.
 * @property screenConverter The name and package of the [ScreenConverter] generated for the [patterns].
 * @property constructorParams Information about the [targetClass] constructor parameters.
 */
public data class DeeplinkInformation(
    val classDeclaration: KSClassDeclaration,
    val targetClass: ClassName,
    val screenConverter: ClassName,
    val patterns: List<String>,
    val placeholders: List<String>,
    val targetIsObject: Boolean,
    val constructorParams: List<ConstructorParameter>
)

/**
 * Information for the parameter in the constructor.
 *
 * @property isMarkedNullable Whether the type is nullable.
 * @property hasDefault Whether the parameter has a default value.
 * @property name Parameter name.
 * @property placeholder Parameter name in placeholder,
 * may be absent if there is no placeholder with the same name. It may also be different from [name] if
 * a [DeeplinkParam] annotation with a custom parameter name is specified.
 * @property className Parameter type.
 * @property isEnum Whether the type is Enum.
 */
public data class ConstructorParameter(
    val isMarkedNullable: Boolean,
    val hasDefault: Boolean,
    val name: String,
    val placeholder: String?,
    val className: ClassName,
    val type: TypeName,
    val isEnum: Boolean
)