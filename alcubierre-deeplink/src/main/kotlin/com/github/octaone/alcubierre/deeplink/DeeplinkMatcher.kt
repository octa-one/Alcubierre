package com.github.octaone.alcubierre.deeplink

import com.github.octaone.alcubierre.deeplink.internal.TrieDeeplinkMatcher

/**
 * Entity enclosing the mapping of a deeplink to a given pattern.
 */
public interface DeeplinkMatcher {

    public fun match(deeplink: DeeplinkUri): DeeplinkMatch?
}

public fun DeeplinkMatcher(deeplinkUriList: List<DeeplinkUri>): DeeplinkMatcher = TrieDeeplinkMatcher(deeplinkUriList)

/**
 * The result of matching a deeplink with a pattern.
 * @property matchedPattern the pattern of the diplink with which the match occurred
 * @property placeholders placeholders from path and query
 */
public data class DeeplinkMatch(val matchedPattern: String, val placeholders: Map<String, String>)
