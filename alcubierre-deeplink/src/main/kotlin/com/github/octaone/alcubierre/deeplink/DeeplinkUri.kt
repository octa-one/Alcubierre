package com.github.octaone.alcubierre.deeplink

data class DeeplinkUri(
    val pattern: String,
    val scheme: String,
    val host: String,
    val path: String?,
    val query: Map<String, String> = emptyMap()
) {
    val queryParameterNames = query.keys

    val pathSegments = path?.split("/").orEmpty()

    init {
        require(scheme.isNotBlank() && host.isNotBlank()) { "Указан пустой scheme/host" }
        require(!scheme.isPlaceholder() && !host.isPlaceholder()) { "Плейсхолдеры в scheme/host не поддерживаются" }
    }

    fun getQueryParameter(name: String) = query[name]

    companion object
}

/**
 * Перед составлением дерева шаблоны сортируются, чтобы обеспечить приоритетность в сопоставлении диплинков
 * к примеру, мэтч по шаблону scheme://concreteHost будет приоритетнее чем scheme://{hostPlaceholder}
 */
fun List<DeeplinkUri>.sortedByPlaceholders() = sortedWith { uri, other ->
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

internal fun String.isPlaceholder() = startsWith('{') && endsWith('}')

internal fun String.extractPlaceholder(): String = substring(1, length - 1)