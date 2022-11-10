import com.github.octaone.alcubierre.deeplink.DeeplinkUri
import com.github.octaone.alcubierre.deeplink.sortedByPlaceholders
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

        val actual = links.map(DeeplinkUri::parse)
            .sortedByPlaceholders()
            .map(DeeplinkUri::pattern)

        assertEquals(expected, actual)
    }
}