package com.github.octaone.alcubierre.codegen.type.converter

import com.github.octaone.alcubierre.codegen.api.PARAM_FROM
import com.github.octaone.alcubierre.codegen.processor.ConstructorParameter
import com.github.octaone.alcubierre.codegen.processor.DeeplinkInformation
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.buildCodeBlock

/**
 * Для дефолтных параметров, заполняемых из диплинка, используется битовая маска
 * если параметр был получен из полученных плейсхолдеров, и параметр != null,
 * то в проставляем бит, с позицией, равной позиции в конструкторе, в 0. Позицию считаем с конца
 *
 * Если все нужные параметры != null, то можем вызвать конструктор напрямую,
 * иначе - вызваем конструктор через reflection
 *
 * Для класса Screen(val a: String = "S", val b: Int = 1)
 * ```
 * var mask = -1
 *
 * val a = _from["a"]
 * if (a != null) mask = mask and 0xfffffffe.toInt()
 *
 * val b = _from["b"]?.toInt()
 * if (b != null) mask = mask and 0xfffffffd.toInt()
 * ```
 *  далее см. [generateReflector]
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
        add("val ${param.name} = ${PARAM_FROM}[\"$placeholder\"]")
        add(typeConversion(param))
        add("\n")

        if (param.hasDefault) {
            val propertySet = (1 shl placeholderIndexes.getValue(placeholder)).inv()
            maskAllSet = maskAllSet and propertySet
            addStatement(
                "if (${param.name} != null) mask = mask and 0x%1L.toInt()\n",
                Integer.toHexString(propertySet)
            )
        } else {
            addStatement("")
        }
    }

    add(generateReflector(maskAllSet, info.targetClass, info.constructorParams))
}

/**
 * Для класса Screen(val a: String = "S", val b: Int = 1)
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

    for (p in constructorParameters) {
        val statement = when {
            p.type.copy(nullable = false).isPrimitive -> {
                if (p.type.isNullable) "%T::class.javaObjectType" else "%T::class.javaPrimitiveType"
            }
            else -> "%T::class.java"
        }
        addStatement("$statement,", p.className)
    }

    addStatement("Int::class.javaPrimitiveType,") // битовая маска
    add(DEFAULT_CONSTRUCTOR_MARKER_TYPE_BLOCK)

    unindent()
    addStatement("\n)")

    add("constructor.newInstance")

    val names = buildList {
        constructorParameters.mapTo(this) { param ->
            buildString {
                append(if (param.placeholder != null) param.name else null)
                val type = param.type
                if (type.isPrimitive) append(" ?: ${type.defaultPrimitiveValue()}")
            }
        }
        add("mask")
        add(null)
    }

    addStatement(names.joinToString(separator = ",\n", prefix = "(", postfix = ")"))
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
        if (!param.isMarkedNullable) add("!!")
        addStatement(",")
    }

    unindent()
    addStatement(")")
}

private val DEFAULT_CONSTRUCTOR_MARKER_TYPE_BLOCK = CodeBlock.of(
    "%M",
    MemberName("com.github.octaone.alcubierre.codegen.api", "DEFAULT_CONSTRUCTOR_MARKER")
)
