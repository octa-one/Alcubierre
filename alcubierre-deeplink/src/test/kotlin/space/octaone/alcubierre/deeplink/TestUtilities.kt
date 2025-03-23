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

package space.octaone.alcubierre.deeplink

import org.junit.Assert
import org.junit.Test
import space.octaone.alcubierre.deeplink.util.sortedByPlaceholders

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
