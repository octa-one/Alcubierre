package com.github.octaone.alcubierre.deeplink.test

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class ScreenConverterTest {

    @Test
    fun `annotated object`() {
        assertEquals(
            ObjectScreen,
            ObjectScreenConverter().convert(mapOf("id" to "1"))
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
            BasicConversionScreenConverter().convert(
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
            EnumScreen(EnumScreen.Mode.A),
            EnumScreenConverter().convert(mapOf("mode" to "A"))
        )
    }

    @Test
    fun `custom param name`() {
        assertEquals(
            CustomNamedScreen("guid"),
            CustomNamedScreenConverter().convert(mapOf("_ID" to "guid"))
        )
    }

    @Test
    fun `screen with default parameters, that not contains in deeplink`() {
        assertEquals(
            DefaultParamScreen("name", listOf(1)),
            DefaultParamScreenConverter().convert(mapOf("name" to "name"))
        )
    }

    @Test
    fun `screen with default parameters, that exists in deeplink`() {
        assertEquals(
            ReflectionScreen("defaultName", "last", "123", "456", null),
            ReflectionScreenConverter().convert(mapOf("lastName" to "last"))
        )
    }

    @Test
    fun `screen with nullable parameters`() {
        assertEquals(
            NullableScreen(null, null, null, null, null, null, null, null),
            NullableScreenConverter().convert(emptyMap())
        )
    }

    @Test
    fun `screen with nullable default parameters`() {
        assertEquals(
            NullableWithDefaultScreen(
                a = "default",
                b = 1,
                c = 2,
                d = 3,
                e = 4,
                f = 5f,
                g = 6.0,
                h = false
            ),
            NullableWithDefaultScreenConverter().convert(mapOf())
        )
    }

    @Test
    fun `screen with multiple patterns`() {
        // первый конвертер генерируется для шаблонов "scheme://host/{id}" и "https://host/{id}"
        assertEquals(
            MultiplePatternScreen("1"),
            MultiplePatternScreenConverter().convert(mapOf("id" to "1"))
        )

        // второй конвертер генерируется для шаблона "scheme://host"
        assertEquals(
            MultiplePatternScreen("default"),
            MultiplePatternScreenConverter1().convert(emptyMap())
        )
    }
}