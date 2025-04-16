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

import android.net.Uri
import space.octaone.alcubierre.core.screen.extra.ExtrasContainer
import space.octaone.alcubierre.deeplink.processor.api.ScreenConverter
import space.octaone.alcubierre.screen.DEEPLINK_URI

/**
 * Create final navigational targets based on the received deeplink.
 * This is done in two steps:
 * First, a matching pattern is searched with [DeeplinkMatcher], then a navigation target (Screen, Dialog, etc.)
 * is created using the corresponding converter from the [converters] map.
 */
public class DeeplinkResolver(
    private val matcher: DeeplinkMatcher,
    private val converters: Map<String, ScreenConverter>
) {

    /**
     * Create final navigational targets based on the received deeplink.
     */
    public fun resolve(deeplink: Uri): Result<Any> = runCatching {
        val uri = DeeplinkUri.parse(deeplink)
        val (pattern, placeholders) = requireNotNull(matcher.match(uri)) { "Screen not found for: $uri" }
        val converter = requireNotNull(converters[pattern]) { "Converter not found for: $pattern" }
        converter.convert(placeholders).withDeeplinkExtra(deeplink)
    }

    private fun Any.withDeeplinkExtra(deeplink: Uri): Any = apply {
        if (this is ExtrasContainer) {
            extras.putParcelable(DEEPLINK_URI, deeplink)
        }
    }
}
