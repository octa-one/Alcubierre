package com.github.octaone.alcubierre.deeplink

import android.net.Uri
import com.github.octaone.alcubierre.deeplink.util.isPlaceholder

/**
 * A simple Uri representation.
 * Used in annotation processing and unit testing without Android SDK.
 * @see Uri
 */
@ConsistentCopyVisibility
public data class DeeplinkUri internal constructor(
    val pattern: String,
    val scheme: String,
    val host: String,
    val pathSegments: List<String>,
    val query: Map<String, String>
) {

    init {
        require(!scheme.isPlaceholder() && !host.isPlaceholder()) {
            "Placeholders in the scheme or host are not supported."
        }
    }

    val path: String
        get() = pathSegments.joinToString("/")

    public companion object {

        /**
         * Create [DeeplinkUri] from String.
         */
        public fun parse(uri: String): DeeplinkUri =
            parse(Uri.parse(uri))

        /**
         * Create [DeeplinkUri] from [Uri].
         */
        public fun parse(uri: Uri): DeeplinkUri =
            DeeplinkUri(
                pattern = uri.toString(),
                scheme = requireNotNull(uri.scheme),
                host = requireNotNull(uri.host),
                pathSegments = uri.pathSegments,
                query = uri.queryParameterNames.associateWith { uri.getQueryParameter(it)!! }
            )
    }
}
