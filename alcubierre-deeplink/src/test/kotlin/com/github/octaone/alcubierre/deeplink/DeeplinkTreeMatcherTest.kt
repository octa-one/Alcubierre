import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class DeeplinkTreeMatcherTest {

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

        // matchedPattern должен соответствовать оригиналу, объявленному в аннотации
        // а не строке, с которой произошел мэтч
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
