package com.github.octaone.alcubierre.codegen.test

import com.github.octaone.alcubierre.codegen.test.EnumScreen.Mode
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class ScreenConverterTest {

    @Test
    fun `annotated object`() {
        assertEquals(
            ObjectScreen,
            ObjectScreen_Converter().convert(mapOf("id" to "1"))
        )
    }

    @Test
    fun `basic types conversion`() {
        assertEquals(
            BasicConversionScreen(
                a = "a",
                b = 129,
                c = 3.toByte(),
                d = 123L,
                e = 1.toShort(),
                f = 4f,
                g = 1.2,
                h = true,
                i = BigDecimal.TEN
            ),
            BasicConversionScreen_Converter().convert(
                mapOf(
                    "a" to "a",
                    "b" to "129",
                    "c" to "3",
                    "d" to "123",
                    "e" to "1",
                    "f" to "4f",
                    "g" to "1.2",
                    "h" to "true",
                    "i" to "10"
                )
            )
        )
    }

    @Test
    fun `enum in params`() {
        assertEquals(
            EnumScreen(Mode.A),
            EnumScreen_Converter().convert(mapOf("mode" to "A"))
        )
    }

    @Test
    fun `custom param name`() {
        assertEquals(
            CustomNamedScreen("guid"),
            CustomNamedScreen_Converter().convert(mapOf("_ID" to "guid"))
        )
    }

    @Test
    fun `screen with default parameters, that not contains in deeplink`() {
        assertEquals(
            DefaultParamScreen("name", listOf(1)),
            DefaultParamScreen_Converter().convert(mapOf("name" to "name"))
        )
    }

    @Test
    fun `screen with default parameters, that exists in deeplink`() {
        assertEquals(
            ReflectionScreen("defaultName", "last", "123", "456", null),
            ReflectionScreen_Converter().convert(mapOf("lastName" to "last"))
        )
    }

    @Test
    fun `screen with nullable parameters`() {
        assertEquals(
            NullableScreen(null),
            NullableScreen_Converter().convert(emptyMap())
        )
    }

    @Test
    fun `screen with multiple patterns`() {
        // первый конвертер генерируется для шаблонов "scheme://host/{id}" и "https://host/{id}"
        assertEquals(
            MultiplePatternScreen("1"),
            MultiplePatternScreen_Converter().convert(mapOf("id" to "1"))
        )

        // второй конвертер генерируется для шаблона "scheme://host"
        assertEquals(
            MultiplePatternScreen("default"),
            MultiplePatternScreen_Converter1().convert(emptyMap())
        )
    }
}