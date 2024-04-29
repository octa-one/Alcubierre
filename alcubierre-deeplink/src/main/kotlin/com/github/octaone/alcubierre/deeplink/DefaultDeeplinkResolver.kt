@file:Suppress("FunctionName")

package com.github.octaone.alcubierre.deeplink

import com.github.octaone.alcubierre.deeplink.processor.api.DeeplinkRegistry
import com.github.octaone.alcubierre.deeplink.processor.api.ScreenConverter
import com.github.octaone.alcubierre.deeplink.util.sortedByPlaceholders

/**
 * Default [DeeplinkResolver].
 * @param registries A list of all [DeeplinkRegistry] in the app.
 * [DeeplinkResolver] is the entry point for the received deeplinks,
 * so in order to successfully match all declared links, all registries must be collected here.
 */
public fun DefaultDeeplinkResolver(registries: List<DeeplinkRegistry>): DeeplinkResolver {
    val patterns = registries
        .flatMap { it.screenConverters.keys }
        .map(DeeplinkUri::parse)
        .sortedByPlaceholders()

    val matcher = DeeplinkMatcher(patterns)
    val converters = HashMap<String, ScreenConverter>()
    for (registry in registries) {
        converters += registry.screenConverters
    }

    return DeeplinkResolver(matcher, converters)
}
