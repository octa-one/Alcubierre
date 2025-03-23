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

package space.octaone.alcubierre.deeplink.util

import space.octaone.alcubierre.deeplink.DeeplinkUri

/**
 * Checks if String is a placeholder: "{placeholder}".
 */
internal fun String.isPlaceholder(): Boolean =
    startsWith('{') && endsWith('}')

/**
 * Removes the brackets to extract the placeholder value.
 */
internal fun String.extractPlaceholder(): String =
    substring(1, length - 1)

/**
 * Patterns are sorted before the tree is created to ensure that deeplink matching is prioritized.
 * For example, matching of scheme://app:/explicitSegment will be prioritized over scheme://{segmentPlaceholder}.
 */
internal fun List<DeeplinkUri>.sortedByPlaceholders() = sortedWith { uri, other ->
    val mainPathSize = uri.pathSegments.size
    val otherPathSize = other.pathSegments.size

    for (i in 0 until minOf(mainPathSize, otherPathSize)) {
        val left = uri.pathSegments[i]
        val right = other.pathSegments[i]

        if (left.isPlaceholder() && !right.isPlaceholder()) return@sortedWith 1
        if (!left.isPlaceholder() && right.isPlaceholder()) return@sortedWith -1
    }

    mainPathSize - otherPathSize
}
