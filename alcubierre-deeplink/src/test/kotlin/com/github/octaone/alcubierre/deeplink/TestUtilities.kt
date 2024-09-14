package com.github.octaone.alcubierre.deeplink

import org.junit.Assert
import org.junit.Test
import com.github.octaone.alcubierre.deeplink.util.sortedByPlaceholders

fun matcher(vararg links: String) =
    DeeplinkMatcher(links.map(::parseDeeplinkForTest).sortedByPlaceholders())

fun DeeplinkMatcher.match(string: String) = match(parseDeeplinkForTest(string))

// Simple version of android.net.Uri::parse, because we can't use Android classes in tests.
internal fun parseDeeplinkForTest(value: String): DeeplinkUri {
    val pattern = if (value.contains('?')) value else "$value?"

    val (scheme, hostPathQuery, queryString) = pattern.split("://", "?")
    val hostPath = hostPathQuery.split("/", limit = 2)

    val query = queryString
        .split("&")
        .map { it.split("=") }
        .filter { it.size == 2 }
        .associate { (key, value) -> key to value }

    return DeeplinkUri(
        pattern = value,
        scheme = scheme,
        host = hostPath[0],
        pathSegments = hostPath.getOrNull(1)?.split("/")?.filter(String::isNotEmpty).orEmpty(),
        query = query
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
                listOf("path", "details"),
                mapOf("from" to "f", "to" to "t")
            ),
            parseDeeplinkForTest(pattern)
        )
    }
}
