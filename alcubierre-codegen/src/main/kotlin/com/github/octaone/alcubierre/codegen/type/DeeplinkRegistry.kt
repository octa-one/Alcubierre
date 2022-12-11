package com.github.octaone.alcubierre.codegen.type

import com.github.octaone.alcubierre.codegen.processor.DeeplinkInformation
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
 * Генерация registry deeplink - screen converter
 *
 * ```
 * public class DeeplinkRegistry {
 *     val screenConverters = mapOf(
 *          "scheme1://host" to FeatureScreen1_Converter(),
 *          "scheme2://host" to FeatureScreen2_Converter(),
 *     )
 * }
 * ```
 */
fun generateDeeplinkRegistry(
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
    beginControlFlow("buildMap")
    for ((i, info) in deeplinkAnnotated.withIndex()) {
        val patterns = info.patterns
        val converter = info.screenConverter
        if (patterns.size == 1) {
            addStatement("put(%S, %T())", patterns.first(), converter)
        } else {
            // на один экран может быть заведено два шаблона, а конвертер будет один и тот же
            // потому что у шаблонов оказался одинаковый набор плейсхолдеров
            // заводим переменную, чтобы не создавать конвертер несколько раз
            addStatement("val converter$i = %T()", converter)
            for (pattern in patterns) addStatement("put(%S, converter$i)", pattern)
        }
    }
    endControlFlow()
}

private fun String.toCamelCase() = split("[\\W_-]+".toRegex()).joinToString("") { s ->
    s.replaceFirstChar { it.titlecase(Locale.US) }
}
private const val PACKAGE = "com.github.octaone.alcubierre"
private const val API_PACKAGE = "$PACKAGE.codegen.api"
val DEEPLINK_REGISTRY = ClassName(API_PACKAGE, "DeeplinkRegistry")
val SCREEN_CONVERTER = ClassName(API_PACKAGE, "ScreenConverter")
val SCREEN = ClassName("$PACKAGE.screen", "Screen")
val DIALOG = ClassName("$PACKAGE.screen", "Dialog")
