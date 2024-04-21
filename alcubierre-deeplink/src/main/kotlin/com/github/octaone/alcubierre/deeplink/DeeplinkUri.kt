package com.github.octaone.alcubierre.deeplink

import android.net.Uri
import com.github.octaone.alcubierre.deeplink.util.isPlaceholder

public data class DeeplinkUri(
    val pattern: String,
    val scheme: String,
    val host: String,
    val path: String?,
    val query: Map<String, String> = emptyMap()
) {

    init {
        require(scheme.isNotBlank() && host.isNotBlank()) { "Указан пустой scheme/host" }
        require(!scheme.isPlaceholder() && !host.isPlaceholder()) { "Плейсхолдеры в scheme/host не поддерживаются" }
    }

    public val pathSegments: List<String> = path?.split("/")?.filter(String::isNotEmpty).orEmpty()

    public companion object {

        public fun parse(uri: String): DeeplinkUri = parse(Uri.parse(uri))

        public fun parse(uri: Uri): DeeplinkUri =
            DeeplinkUri(
                pattern = uri.toString(),
                scheme = requireNotNull(uri.scheme),
                host = requireNotNull(uri.host),
                path = uri.path,
                query = uri.queryParameterNames.associateWith { uri.getQueryParameter(it)!! }
            )
    }
}
