package space.octaone.alcubierre.util

import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import java.util.Collections
import kotlin.reflect.KClass

@AlcubierreInternalApi
public inline fun <reified V : Any> Map<KClass<*>, *>.getAndCast(): V? = get(V::class) as? V

internal fun <K, V> Map<K, V>.optimizeReadOnlyMap() = when (size) {
    0 -> emptyMap()
    1 -> with(iterator().next()) { Collections.singletonMap(key, value) }
    else -> this
}
