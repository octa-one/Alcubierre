package com.github.octaone.alcubierre.deeplink.util

import com.github.octaone.alcubierre.deeplink.DeeplinkUri

internal fun String.isPlaceholder(): Boolean = startsWith('{') && endsWith('}')

internal fun String.extractPlaceholder(): String = substring(1, length - 1)


/**
 * Перед составлением дерева шаблоны сортируются, чтобы обеспечить приоритетность в сопоставлении диплинков
 * к примеру, мэтч по шаблону scheme://concreteHost будет приоритетнее чем scheme://{hostPlaceholder}
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
