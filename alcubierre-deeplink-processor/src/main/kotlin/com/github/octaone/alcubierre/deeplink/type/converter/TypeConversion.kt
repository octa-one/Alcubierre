package com.github.octaone.alcubierre.deeplink.type.converter

import com.github.octaone.alcubierre.deeplink.processor.ConstructorParameter
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Generating type conversion code:
 * val x = _from["X"] -> val x = _from["X"].toBigDecimal()
 */
public fun typeConversion(param: ConstructorParameter): CodeBlock = buildCodeBlock {
    if (param.className != STRING) add("?")

    when {
        param.isEnum -> add(".let(%T::valueOf)", param.className)
        else -> {
            val conversion = requireNotNull(DEFAULT_CONVERSIONS[param.className]) {
                "Type ${param.className.simpleName} can not be created from a deeplink. Supported types: ${DEFAULT_CONVERSIONS.keys} and Enums"
            }
            add(conversion)
        }
    }

    if (!param.isMarkedNullable && !param.hasDefault) {
        add("\n    ?: throw IllegalArgumentException(\"${param.placeholder} is missing\")")
    }
}

private val DEFAULT_CONVERSIONS = mapOf(
    INT to ".toInt()",
    CHAR to ".toChar()",
    BYTE to ".toByte()",
    LONG to ".toLong()",
    SHORT to ".toShort()",
    FLOAT to ".toFloat()",
    DOUBLE to ".toDouble()",
    BOOLEAN to ".toBoolean()",
    STRING to "",
    BigInteger::class.asClassName() to ".toBigInteger()",
    BigDecimal::class.asClassName() to ".toBigDecimal()",
)

internal val PRIMITIVES = listOf(BOOLEAN, CHAR, BYTE, SHORT, INT, FLOAT, LONG, DOUBLE)

internal val TypeName.isPrimitive get() = this.copy(nullable = false) in PRIMITIVES

internal fun TypeName.defaultPrimitiveValue(): CodeBlock {
    val code = when (this) {
        BOOLEAN -> "false"
        CHAR -> "0.toChar()"
        BYTE -> "0.toByte()"
        SHORT -> "0.toShort()"
        INT -> "0"
        FLOAT -> "0f"
        LONG -> "0L"
        DOUBLE -> "0.0"
        else -> "null"
    }
    return CodeBlock.of(code)
}
