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

package space.octaone.alcubierre.deeplink

import android.net.Uri
import space.octaone.alcubierre.deeplink.util.isPlaceholder

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
