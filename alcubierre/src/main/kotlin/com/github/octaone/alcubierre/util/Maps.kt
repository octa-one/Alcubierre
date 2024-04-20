package com.github.octaone.alcubierre.util

import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import kotlin.reflect.KClass

@AlcubierreInternalApi
inline fun <reified V : Any> Map<KClass<*>, *>.getAndCast(): V? = get(V::class) as? V

internal fun <K, V> Map<K, V>.optimizeReadOnlyMap() = when (size) {
    0 -> emptyMap()
    1 -> with(iterator().next()) { java.util.Collections.singletonMap(key, value) }
    else -> this
}
