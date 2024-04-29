package com.github.octaone.alcubierre.deeplink.type

import com.github.octaone.alcubierre.deeplink.processor.DeeplinkInformation
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.buildCodeBlock
import java.util.Locale

/**
 * Generating a registry for associating deeplinks with converters.
 *
 * ```
 * public class DeeplinkRegistry {
 *     val screenConverters = mapOf(
 *          "scheme://screen1" to FeatureScreen1Converter(),
 *          "scheme://screen2" to FeatureScreen2Converter(),
 *     )
 * }
 * ```
 */
public fun generateDeeplinkRegistry(
    registryBaseName: String,
    deeplinkAnnotated: List<DeeplinkInformation>
): TypeSpec {
    val convertersMap = PropertySpec
        .builder(
            "screenConverters",
            MAP.parameterizedBy(STRING, SCREEN_CONVERTER)
        )
        .addModifiers(KModifier.OVERRIDE)
        .initializer(generateConvertersMapBody(deeplinkAnnotated))
        .build()

    return TypeSpec
        .classBuilder("${registryBaseName.toCamelCase()}DeeplinkRegistry")
        .addSuperinterface(DEEPLINK_REGISTRY)
        .addProperty(convertersMap)
        .build()
}

private fun generateConvertersMapBody(
    deeplinkAnnotated: List<DeeplinkInformation>
) = buildCodeBlock {
    val totalSize = deeplinkAnnotated.sumOf { it.patterns.size }
    beginControlFlow("buildMap($totalSize)")
    deeplinkAnnotated.forEachIndexed { i, info ->
        val patterns = info.patterns
        val converter = info.screenConverter
        if (patterns.size == 1) {
            addStatement("put(%S, %T())", patterns.first(), converter)
        } else {
            addStatement("val converter$i = %T()", converter)
            for (pattern in patterns) addStatement("put(%S, converter$i)", pattern)
        }
    }
    endControlFlow()
}

private fun String.toCamelCase() = split(MODULE_NAME_REGEX)
    .joinToString("") { s -> s.replaceFirstChar { it.titlecase(Locale.ENGLISH) } }

private val MODULE_NAME_REGEX = "[\\W_-]+".toRegex()

private const val PACKAGE = "com.github.octaone.alcubierre"
private const val API_PACKAGE = "$PACKAGE.deeplink.processor.api"
public val DEEPLINK_REGISTRY: ClassName = ClassName(API_PACKAGE, "DeeplinkRegistry")
public val SCREEN_CONVERTER: ClassName = ClassName(API_PACKAGE, "ScreenConverter")

public val FRAGMENT_SCREEN: ClassName = ClassName("$PACKAGE.screen", "FragmentScreen")
public val FRAGMENT_DIALOG: ClassName = ClassName("$PACKAGE.screen", "FragmentDialog")
public val COMPOSE_SCREEN: ClassName = ClassName("$PACKAGE.screen", "ComposeScreen")
public val COMPOSE_DIALOG: ClassName = ClassName("$PACKAGE.screen", "ComposeDialog")
public val CONDITIONAL_TARGET: ClassName = ClassName("$PACKAGE.condition", "ConditionalTarget")

public val KNOWN_TYPES: Array<ClassName> = arrayOf(FRAGMENT_SCREEN, FRAGMENT_DIALOG, COMPOSE_SCREEN, COMPOSE_DIALOG, CONDITIONAL_TARGET)
