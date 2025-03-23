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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class DeeplinkMatcherTest {

    @Test
    fun `basic match`() {
        val matcher = matcher("scheme://path/{id}/{details}?from={from}&to={to}")
        assertNotNull(matcher.match("scheme://path/1/2?from=from&to=to"))
    }

    @Test
    fun `partial match shouldn't be captured`() {
        val matcher = matcher("scheme://host/path/page")
        assertNull(matcher.match("scheme://host/path/page/detail"))
        assertNull(matcher.match("scheme://host/path"))
    }

    @Test
    fun `all query params goes into placeholders`() {
        val matcher = matcher("scheme://host/path?from={fromPlaceholder}&to=notPlaceholder")
        assertEquals(
            mapOf("fromPlaceholder" to "place"),
            matcher.match("scheme://host/path?from=place&to=place1")?.placeholders
        )
    }

    @Test
    fun `shuffled query`() {
        val originalPattern = "scheme://host/path?FROM={arg1}&TO={arg2}"
        val shuffledPattern = "scheme://host/path?TO=arg2&FROM=arg1"
        val matcher = matcher(originalPattern)

        assertEquals(
            originalPattern,
            matcher.match(shuffledPattern)?.matchedPattern
        )
    }

    @Test
    fun `long path priority`() {
        val matcher = matcher("scheme://host/path", "scheme://host/path/detail")
        assertEquals(
            "scheme://host/path/detail",
            matcher.match("scheme://host/path/detail")?.matchedPattern
        )
    }

    @Test
    fun `exact pattern priority`() {
        val matcher = matcher("scheme://host/{place}", "scheme://host/place")
        assertEquals(
            "scheme://host/place",
            matcher.match("scheme://host/place")?.matchedPattern
        )
    }

    @Test
    fun `exact segment priority`() {
        val matcher = matcher(
            "scheme://host/page/{pageId}",
            "scheme://host/{greedy}"
        )

        assertEquals(
            "scheme://host/page/{pageId}",
            matcher.match("scheme://host/page/42")?.matchedPattern
        )
    }

    @Test
    fun `long placeholder path priority`() {
        val matcher = matcher(
            "scheme://host/{arg1}/segment/{arg2}",
            "scheme://host/{arg1}"
        )

        assertEquals(
            "scheme://host/{arg1}/segment/{arg2}",
            matcher.match("scheme://host/123/segment/1234")?.matchedPattern
        )
    }

    @Test
    fun `braces in query`() {
        val matcher = matcher("scheme://x?from={from}&to={to}")
        assertEquals(
            mapOf("from" to "fr{}om", "to" to "{to}"),
            matcher.match("scheme://x?from=fr{}om&to={to}")?.placeholders
        )
    }

    @Test
    fun `placeholders extraction`() {
        val matcher = matcher("scheme://path/{id}/{details}?from={from}&to={to}")
        assertEquals(
            mapOf("id" to "1", "details" to "2", "from" to "from", "to" to "to"),
            matcher.match("scheme://path/1/2?from=from&to=to")?.placeholders
        )
    }
}
