package com.github.octaone.alcubierre.deeplink

import com.github.octaone.alcubierre.deeplink.internal.TrieDeeplinkMatcher

/**
 * Default [DeeplinkMatcher] implementation.
 * @param deeplinkUriList A list of known [DeeplinkUri]s for matching.
 * @see TrieDeeplinkMatcher
 */
public fun DeeplinkMatcher(deeplinkUriList: List<DeeplinkUri>): DeeplinkMatcher =
    TrieDeeplinkMatcher(deeplinkUriList)

/**
 * Interface for matcher.
 */
public interface DeeplinkMatcher {

    /**
     * Matches the given [DeeplinkUri] with known deeplinks.
     * @param deeplink A deeplink to match.
     * @return [DeeplinkMatch]
     *
     * For example:
     * Known deeplink: scheme://app/{id}?action={action}
     * Received deeplink: scheme://app/1?action=SayHello
     * DeeplinkMatch: DeeplinkMatch("scheme://app/{id}?action={action}", mapOf("id" to 1, "action" to "SayHello")))
     */
    public fun match(deeplink: DeeplinkUri): DeeplinkMatch?
}


/**
 * The result of matching a deeplink with a pattern.
 * @property matchedPattern The pattern of the deeplink with whom match occurred.
 * @property placeholders Placeholder values from path and query.
 */
public data class DeeplinkMatch(
    val matchedPattern: String,
    val placeholders: Map<String, String>
)
