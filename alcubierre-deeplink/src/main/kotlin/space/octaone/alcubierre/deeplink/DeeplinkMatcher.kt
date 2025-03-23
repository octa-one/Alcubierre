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

import space.octaone.alcubierre.deeplink.internal.TrieDeeplinkMatcher

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
