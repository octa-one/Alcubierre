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

@file:Suppress("FunctionName")

package space.octaone.alcubierre.deeplink

import space.octaone.alcubierre.deeplink.processor.api.DeeplinkRegistry
import space.octaone.alcubierre.deeplink.processor.api.ScreenConverter
import space.octaone.alcubierre.deeplink.util.sortedByPlaceholders

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
