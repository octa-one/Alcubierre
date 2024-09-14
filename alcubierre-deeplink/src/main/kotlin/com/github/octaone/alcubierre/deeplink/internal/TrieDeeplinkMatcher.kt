package com.github.octaone.alcubierre.deeplink.internal

import android.os.Build
import com.github.octaone.alcubierre.deeplink.DeeplinkMatch
import com.github.octaone.alcubierre.deeplink.DeeplinkMatcher
import com.github.octaone.alcubierre.deeplink.DeeplinkUri
import com.github.octaone.alcubierre.deeplink.util.extractPlaceholder
import com.github.octaone.alcubierre.deeplink.util.isPlaceholder
import java.net.URLDecoder

/**
 * [DeeplinkMatcher], based on [Trie](https://en.wikipedia.org/wiki/Trie).
 *
 * @author Alexander Perfilyev
 */
internal class TrieDeeplinkMatcher(uris: List<DeeplinkUri>) : DeeplinkMatcher {

    private val root = TrieNode()

    init {
        for (i in uris.indices) {
            val uri = uris[i]
            insert(uri, uri.toList())
        }
    }

    override fun match(deeplink: DeeplinkUri): DeeplinkMatch? {
        val values = deeplink.toList()
        var placeholders: MutableMap<String, String>? = null
        var current = root

        for (i in values.indices) {
            val key = values[i]
            val children = current.children
            current = if (children.containsKey(key)) {
                children[key]!!
            } else {
                val placeholder = current.childPlaceholder
                if (placeholder != null) {
                    val removeSurrounding = placeholder.removeSurrounding("{", "}")
                    if (placeholders == null) {
                        placeholders = mutableMapOf()
                    }
                    placeholders[removeSurrounding] = decode(key)
                    children[placeholder]!!
                } else {
                    return null
                }
            }
        }

        val uri = current.uri ?: return null

        for ((k, v) in uri.query) {
            if (!v.isPlaceholder()) continue

            val placeholder = v.extractPlaceholder()
            val value = deeplink.query[k]
            if (value != null) {
                if (placeholders == null) {
                    placeholders = mutableMapOf()
                }
                placeholders[placeholder] = decode(value)
            }
        }
        return DeeplinkMatch(uri.pattern, placeholders.orEmpty())
    }

    private fun insert(uri: DeeplinkUri, values: List<String>) {
        var current = root
        for (i in values.indices) {
            var key = values[i]
            if (key.isPlaceholder()) {
                key = current.childPlaceholder ?: run {
                    current.childPlaceholder = key
                    key
                }
            }

            current = current.children.getOrPut(key, TrieDeeplinkMatcher::TrieNode)
        }
        current.uri = uri
    }

    private fun DeeplinkUri.toList(): List<String> =
        buildList(1 + 1 + pathSegments.size) {
            add(scheme)
            add(host)
            addAll(pathSegments)
        }

    private fun decode(s: String): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            URLDecoder.decode(s, Charsets.UTF_8)
        } else {
            URLDecoder.decode(s, "UTF-8")
        }

    private class TrieNode {
        var childPlaceholder: String? = null
        val children = mutableMapOf<String, TrieNode>()
        var uri: DeeplinkUri? = null
    }
}
