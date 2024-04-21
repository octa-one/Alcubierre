package com.github.octaone.alcubierre.deeplink

import com.github.octaone.alcubierre.deeplink.processor.api.DeeplinkRegistry
import com.github.octaone.alcubierre.deeplink.processor.api.ScreenConverter
import com.github.octaone.alcubierre.deeplink.util.sortedByPlaceholders

public fun DefaultDeeplinkResolver(registries: List<DeeplinkRegistry>): DeeplinkResolver {
    val patterns = registries
        .flatMap { it.screenConverters.keys }
        .map { DeeplinkUri.parse(it) }
        .sortedByPlaceholders()

    val matcher = DeeplinkMatcher(patterns)
    val converters = HashMap<String, ScreenConverter>()
    for (registry in registries) {
        converters += registry.screenConverters
    }

    return DeeplinkResolver(matcher, converters)
}
