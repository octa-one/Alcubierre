package com.github.octaone.alcubierre.codegen.type.converter

import com.github.octaone.alcubierre.codegen.processor.ConstructorParameter
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import java.math.BigDecimal

/**
 * Добавить к переменной код, конвертирующий ее в нужный тип
 * val x = from["X"] -> val x = from["X"].toBigDecimal()
 */
fun typeConversion(param: ConstructorParameter) = buildCodeBlock {
    if (param.className != STRING) add("?")

    when {
        param.isEnum -> add(".let(%T::valueOf)", param.className)
        else -> {
            val conversion = requireNotNull(DEFAULT_CONVERSIONS[param.className]) {
                "Тип ${param.className.simpleName} не поддерживается. Доступны ${DEFAULT_CONVERSIONS.keys} и enum типы"
            }
            add(conversion)
        }
    }

    if (!param.isMarkedNullable && !param.hasDefault) {
        add("\n    ?: throw IllegalArgumentException(\"Параметр \$from отсутсвует в плейсхолдерах ${param.placeholder}\")")
    }
}

private val DEFAULT_CONVERSIONS = mapOf(
    INT to ".toInt()",
    BYTE to ".toByte()",
    LONG to ".toLong()",
    SHORT to ".toShort()",
    FLOAT to ".toFloat()",
    DOUBLE to ".toDouble()",
    BOOLEAN to ".toBoolean()",
    STRING to "",
    BigDecimal::class.asClassName() to ".toBigDecimal()",
)
