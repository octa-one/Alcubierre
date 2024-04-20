package com.github.octaone.alcubierre.deeplink

import com.github.octaone.alcubierre.codegen.api.DeeplinkRegistry
import com.github.octaone.alcubierre.codegen.api.ScreenConverter

fun DefaultDeeplinkResolver(registries: List<DeeplinkRegistry>): DeeplinkResolver {
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
