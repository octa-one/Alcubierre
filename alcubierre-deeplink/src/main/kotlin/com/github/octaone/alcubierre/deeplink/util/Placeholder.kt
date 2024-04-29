package com.github.octaone.alcubierre.deeplink.util

import com.github.octaone.alcubierre.deeplink.DeeplinkUri

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
