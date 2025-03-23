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
