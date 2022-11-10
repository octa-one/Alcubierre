import com.github.octaone.alcubierre.deeplink.DeeplinkUri
import com.github.octaone.alcubierre.deeplink.DeeplinkTreeMatcher
import com.github.octaone.alcubierre.deeplink.DeeplinkTreeRoot
import com.github.octaone.alcubierre.deeplink.sortedByPlaceholders
import org.junit.Assert
import org.junit.Test

fun matcher(
    vararg links: String
) = DeeplinkTreeMatcher(root(*links))

fun root(vararg uris: String) = DeeplinkTreeRoot(uris.map(DeeplinkUri::parse).sortedByPlaceholders())

fun DeeplinkTreeMatcher.match(string: String) = match(DeeplinkUri.parse(string))

// в реальном коде диплинк будет парситься через android.net.Uri, который недоступен в unit тестах
// поэтому тут написана собственная, урезанная, реализация
fun DeeplinkUri.Companion.parse(value: String): DeeplinkUri {
    val pattern = if (value.contains('?')) value else "$value?"

    val (scheme, hostPathQuery, queryString) = pattern.split("://", "?")
    val hostPath = hostPathQuery.split("/", limit = 2)

    val query = queryString
        .split("&")
        .map { it.split("=") }
        .filter { it.size == 2 }
        .associate { (key, value) -> key to value }

    return DeeplinkUri(
        value, scheme, hostPath[0], hostPath.getOrNull(1), query
    )
}

class DeeplinkUriParserTest {

    @Test
    fun basic() {
        val pattern = "scheme://host/path/details?from=f&to=t"
        Assert.assertEquals(
            DeeplinkUri(
                pattern,
                "scheme",
                "host",
                "path/details",
                mapOf("from" to "f", "to" to "t")
            ),
            DeeplinkUri.parse(pattern)
        )
    }
}