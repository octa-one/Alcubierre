package com.github.octaone.alcubierre.deeplink

import android.net.Uri
import com.github.octaone.alcubierre.codegen.api.DeeplinkRegistry
import com.github.octaone.alcubierre.codegen.api.ScreenConverter
import com.github.octaone.alcubierre.screen.DEEPLINK_URI
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer

/**
 * Класс, собирает воедино [DeeplinkRegistry] со всех модулей
 * и на основе общего регистра сопоставляет диплинк и [Screen]
 */
class DeeplinkResolver(
    private val matcher: DeeplinkMatcher,
    private val converters: Map<String, ScreenConverter>
) {

    fun resolve(deeplink: Uri): Result<Any> = runCatching {
        val uri = DeeplinkUri.parse(deeplink)
        val (pattern, placeholders) = requireNotNull(matcher.match(uri)) { "Для диплинка $uri не найден экран" }
        val converter = requireNotNull(converters[pattern]) { "У шаблона $pattern нет конвертера" }
        converter.convert(placeholders).withDeeplinkExtra(deeplink)
    }

    private fun Any.withDeeplinkExtra(deeplink: Uri): Any = apply {
        if (this is ExtrasContainer) {
            extras.putParcelable(DEEPLINK_URI, deeplink)
        }
    }
}
