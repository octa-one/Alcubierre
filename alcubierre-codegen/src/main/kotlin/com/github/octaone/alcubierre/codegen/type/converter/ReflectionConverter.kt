package com.github.octaone.alcubierre.codegen.type.converter

import com.github.octaone.alcubierre.codegen.api.PARAM_FROM
import com.github.octaone.alcubierre.codegen.processor.ConstructorParameter
import com.github.octaone.alcubierre.codegen.processor.DeeplinkInformation
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.buildCodeBlock

/**
 * A bitmask is used for default parameters filled from the deeplink.
 * If the parameter was obtained from the received placeholders, and the parameter != null,
 * then the bit with position equals to the position in the constructor is set to 0.
 * The position is counted from the end.
 *
 * If all necessary parameters != null, we can call the constructor directly,
 * otherwise we call the constructor with reflection.
 *
 * Generated for class Screen(val a: String = "S", val b: Int = 1)
 * ```
 * var mask = -1
 *
 * val a = _from["a"]
 * if (a != null) mask = mask and 0xfffffffe.toInt()
 *
 * val b = _from["b"]?.toInt()
 * if (b != null) mask = mask and 0xfffffffd.toInt()
 * ```
 *  see [generateReflector]
 */
fun generateReflectionConverter(info: DeeplinkInformation) = buildCodeBlock {
    val paramToPlaceholder = info.constructorParams
        .filter { it.placeholder != null }
        .associateWith { requireNotNull(it.placeholder) }

    val placeholderIndexes = info.constructorParams.mapIndexedNotNull { i, param ->
        if (param.placeholder != null) param.placeholder to i else null
    }.toMap()

    var maskAllSet = -1 // значение маски, если все параметры были != null

    addStatement("var mask = -1\n") // 0xffffffff

    for ((param, placeholder) in paramToPlaceholder) {
        add("val ${param.name} = $PARAM_FROM[\"$placeholder\"]")
        add(typeConversion(param))
        add("\n")

        if (param.hasDefault) {
            val propertySet = (1 shl placeholderIndexes.getValue(placeholder)).inv()
            maskAllSet = maskAllSet and propertySet
            add(
                "if (${param.name} != null) mask = mask and 0x%1L.toInt()\n\n",
                Integer.toHexString(propertySet)
            )
        } else {
            add("\n")
        }
    }

    add(generateReflector(maskAllSet, info.targetClass, info.constructorParams))
}

/**
 * Generated for class Screen(val a: String = "S", val b: Int = 1)
 *
 * ```
 *  return if (mask == 0xfffffffc.toInt()) {
 *      Screen(a = a, b = b)
 *  } else {
 *      val constructor = Screen::class.java.getDeclaredConstructor(
 *          String::class.java, // a
 *          Int::class.java, // b
 *          Int::class.java, // mask
 *          DEFAULT_CONSTRUCTOR_MARKER
 *      )
 *      constructor.newInstance(a, b, mask, null)
 *  }
 * ```
 */
private fun generateReflector(
    maskAllSet: Int,
    targetClass: ClassName,
    constructorParameters: List<ConstructorParameter>
) = buildCodeBlock {

    beginControlFlow("return if (mask == 0x${Integer.toHexString(maskAllSet)}.toInt())")
    add(generateBasicConstructor(targetClass, constructorParameters))
    nextControlFlow("else")

    add("val constructor = %T::class.java.getDeclaredConstructor(\n", targetClass)
    indent()

    for (param in constructorParameters) {
        val statement = when {
            param.type.isPrimitive && param.type.isNullable -> "%T::class.javaObjectType"
            else -> "%T::class.java"
        }
        add("$statement,\n", param.className)
    }

    add("Int::class.java,\n")
    add(DEFAULT_CONSTRUCTOR_MARKER_TYPE_BLOCK)
    add("\n")

    unindent()
    add(")")

    add("\n")

    add("constructor.newInstance(\n")
    indent()

    for (param in constructorParameters) {
        if (param.type.isPrimitive) {
            if (param.placeholder != null) {
                add(param.name)
                add(" ?: ")
            }
            add(param.type.defaultPrimitiveValue())
        } else {
            if (param.placeholder != null) {
                add(param.name)
            } else {
                add("null")
            }
        }
        add(",\n")
    }
    add("mask,\n")
    add("null\n")

    unindent()
    add(")")

    add("\n")
    endControlFlow()
}

private fun generateBasicConstructor(
    targetClass: ClassName,
    params: List<ConstructorParameter>
) = buildCodeBlock {

    addStatement("%T(", targetClass)
    indent()

    for (param in params.filter { it.placeholder != null }) {
        add("${param.name} = ${param.name}")
        if (!param.isMarkedNullable && param.hasDefault) add("!!")
        addStatement(",")
    }

    unindent()
    addStatement(")")
}

private val DEFAULT_CONSTRUCTOR_MARKER_TYPE_BLOCK = CodeBlock.of(
    "%M",
    MemberName("com.github.octaone.alcubierre.codegen.api", "DEFAULT_CONSTRUCTOR_MARKER")
)
