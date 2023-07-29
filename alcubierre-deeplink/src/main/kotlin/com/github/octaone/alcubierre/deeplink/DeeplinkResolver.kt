package com.github.octaone.alcubierre.deeplink

import android.net.Uri
import com.github.octaone.alcubierre.ExtrasKeys
import com.github.octaone.alcubierre.codegen.api.ScreenConverter
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer

/**
 * Класс, собирает воедино [DeeplinkRegistry] со всех модулей
 * и на основе общего регистра сопоставляет диплинк и [Screen]
 */
class DeeplinkResolver {

    private val matcher: DeeplinkMatcher

    private val converters = mutableMapOf<String, ScreenConverter>()

    init {
        val colony = DeeplinkColony()

        val patterns = colony.registries
            .flatMap { it.screenConverters.keys }
            .map { Uri.parse(it).toDeeplinkUri() }
            .sortedByPlaceholders()

        matcher = DeeplinkMatcher(patterns)

        for (registry in colony.registries) converters += registry.screenConverters
    }

    fun resolve(deeplink: Uri): Any {
        val uri = deeplink.toDeeplinkUri()
        val (pattern, placeholders) = requireNotNull(matcher.match(uri)) { "Для диплинка $uri не найден экран" }
        val converter = requireNotNull(converters[pattern]) { "У шаблона $pattern нет конвертера" }
        return converter.convert(placeholders).withDeeplinkExtra(deeplink)
    }

    private fun Uri.toDeeplinkUri() = DeeplinkUri(
        pattern = toString(),
        scheme = requireNotNull(scheme),
        host = requireNotNull(host),
        path = path,
        query = queryParameterNames.associateWith { requireNotNull(getQueryParameter(it)) }
    )

    private fun Any.withDeeplinkExtra(deeplink: Uri): Any = apply {
        if (this is ExtrasContainer) {
            extras.putParcelable(ExtrasKeys.DEEPLINK_URI, deeplink)
        }
    }
}
