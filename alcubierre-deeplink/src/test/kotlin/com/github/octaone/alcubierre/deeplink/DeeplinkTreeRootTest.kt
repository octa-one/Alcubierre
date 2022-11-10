import org.junit.Assert
import org.junit.Test
import kotlin.test.assertFails

class DeeplinkTreeRootTest {

    @Test
    fun `tail check`() {
        with(root("scheme://host")) {
            val schemeNode = node.childrenList.first()
            val hostNode = schemeNode.childrenList.first()
            Assert.assertTrue(hostNode.matchingUri?.pattern == "scheme://host")
        }
    }

    @Test
    fun `exact duplicates`() {
        assertFails { root("scheme://host/path", "scheme://host/path") }
    }

    @Test
    fun `duplicates with query`() {
        assertFails { root("scheme://host/path", "scheme://host/path?from=place") }
    }

    @Test
    fun `duplicates with placeholders`() {
        assertFails { root("scheme://host/{p}", "scheme://host/{p}?from=place") }
        assertFails { root("scheme://host/{p1}", "scheme://host/{p2}") }
        assertFails { root("scheme://host/{p1}/x", "scheme://host/{p2}/y") }
    }
}