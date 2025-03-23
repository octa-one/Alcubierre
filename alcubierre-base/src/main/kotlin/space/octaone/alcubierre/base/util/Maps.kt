package space.octaone.alcubierre.base.util

import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi

@AlcubierreInternalApi
public fun <K, V> Map<K, V>.getNotNull(key: K): V =
    get(key) ?: throw NoSuchElementException("Key $key is missing in the map.")
