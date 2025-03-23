/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.octaone.alcubierre.deeplink.type.converter

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
import space.octaone.alcubierre.deeplink.processor.ConstructorParameter
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
