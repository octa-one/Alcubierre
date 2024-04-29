package com.github.octaone.alcubierre.deeplink

import android.net.Uri
import com.github.octaone.alcubierre.deeplink.processor.api.ScreenConverter
import com.github.octaone.alcubierre.screen.DEEPLINK_URI
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer

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
