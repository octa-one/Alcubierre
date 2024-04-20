package com.github.octaone.alcubierre.deeplink

import android.net.Uri

data class DeeplinkUri(
    val pattern: String,
    val scheme: String,
    val host: String,
    val path: String?,
    val query: Map<String, String> = emptyMap()
) {

    val pathSegments = path?.split("/").orEmpty().filter(String::isNotEmpty)

    init {
        require(scheme.isNotBlank() && host.isNotBlank()) { "Указан пустой scheme/host" }
        require(!scheme.isPlaceholder() && !host.isPlaceholder()) { "Плейсхолдеры в scheme/host не поддерживаются" }
    }

    fun getQueryParameter(name: String) = query[name]

    companion object {

        fun parse(uri: Uri): DeeplinkUri =
            DeeplinkUri(
                pattern = uri.toString(),
                scheme = requireNotNull(uri.scheme),
                host = requireNotNull(uri.host),
                path = uri.path,
                query = uri.queryParameterNames.associateWith { uri.getQueryParameter(it)!! }
            )

        fun parse(uri: String): DeeplinkUri = parse(Uri.parse(uri))
    }
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

internal fun Map<String, String>.filterPlaceholders() = filter { it.value.isPlaceholder() }
    .mapValues { it.value.extractPlaceholder() }