package com.github.octaone.alcubierre.deeplink

import com.github.octaone.alcubierre.deeplink.util.sortedByPlaceholders
import org.junit.Assert.assertEquals
import org.junit.Test

class UriPatternCompareTest {

    @Test
    fun `placeholders have lower priority in uri sorting`() {
        val links = listOf(
            "app://host/path/{p}",
            "app://host/path/p",
            "app://host/{path}/{p}/{x}",
            "app://host/{path}/p/x",
            "app://host/path",
            "app://host/{path}/p",
            "app://host/{path}/p/{x}",
            "app://host/{path}/{p}",
            "app://host/{path}/{p}/x",
            "app://host",
        )

        val expected = listOf(
            "app://host",

            "app://host/path",
            "app://host/path/p",
            "app://host/path/{p}",

            "app://host/{path}/p",
            "app://host/{path}/p/x",
            "app://host/{path}/p/{x}",

            "app://host/{path}/{p}",
            "app://host/{path}/{p}/x",
            "app://host/{path}/{p}/{x}",
        )

        val actual = links.map(::parseDeeplinkForTest)
            .sortedByPlaceholders()
            .map(DeeplinkUri::pattern)

        assertEquals(expected, actual)
    }
}