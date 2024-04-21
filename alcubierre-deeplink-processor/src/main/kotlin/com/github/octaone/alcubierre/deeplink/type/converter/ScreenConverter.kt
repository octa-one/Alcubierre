package com.github.octaone.alcubierre.deeplink.type.converter

import com.github.octaone.alcubierre.deeplink.processor.api.PARAM_FROM
import com.github.octaone.alcubierre.deeplink.processor.DeeplinkInformation
import com.github.octaone.alcubierre.deeplink.type.SCREEN_CONVERTER
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.buildCodeBlock

/**
 * Generating a converter from a set of placeholders to a screen instance.
 * ```
 * class FeatureScreenConverter : ScreenConverter {
 *      override fun convert(_from: Map<String, String>): Screen = FeatureScreen(
 *          id = _from["ID"],
 *      )
 * }
 * ```
 */
public fun generateConverter(info: DeeplinkInformation): TypeSpec {
    val converterFunction = FunSpec.builder("convert")
        .addModifiers(KModifier.OVERRIDE)
        .addParameter(PARAM_FROM, MAP.parameterizedBy(STRING, STRING))
        .addCode(generateConverterBody(info))
        .addKdoc("Converter for templates:\n${info.patterns.joinToString()}")
        .returns(ANY)
        .build()

    return TypeSpec
        .classBuilder(info.screenConverter)
        .addSuperinterface(SCREEN_CONVERTER)
        .addFunction(converterFunction)
        .build()
}

private fun generateConverterBody(info: DeeplinkInformation): CodeBlock {

    // If an object is annotated with an annotation, we simply return that object
    if (info.targetIsObject) return CodeBlock.of("return %T", info.targetClass)

    // to create an object, all fields of the constructor must be filled in (except those with default values)
    // this means that all such parameters must be declared in placeholders
    for (param in info.constructorParams) {
        if (!param.hasDefault) {
            requireNotNull(param.placeholder) {
                "Параметр ${param.name} не объявлен в плейсхолдерах из ${info.patterns}.\n" +
                        "Укажите все обязательные поля конструктора класса в списке плейсхолдеров"
            }
        }
    }

    val paramWithPlaceholders = info.constructorParams.filter { it.placeholder != null }

    // если заполняемый из диплинка параметр конструктора имеет дефолтное значение
    // то это значение можно потерять, если в диплинке будут отсутствовать данные - нужно создавать объект через reflection,
    // чаще всего это необходимо при работе с опциональными query параметрами
    // в противном случае, достаточно вызова простого конструктора
    return if (paramWithPlaceholders.any { it.hasDefault }) {
        generateReflectionConverter(info)
    } else {
        buildCodeBlock {
            add("return %T(\n", info.targetClass)
            indent()

            for (param in paramWithPlaceholders) {
                add("${param.name} = $PARAM_FROM[\"${param.placeholder}\"]")
                add(typeConversion(param))
                addStatement(",")
            }

            unindent()
            add(")")
        }
    }
}
