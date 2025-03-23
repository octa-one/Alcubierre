package space.octaone.alcubierre.deeplink

import org.junit.Assert.assertEquals
import org.junit.Test
import space.octaone.alcubierre.deeplink.util.sortedByPlaceholders

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
