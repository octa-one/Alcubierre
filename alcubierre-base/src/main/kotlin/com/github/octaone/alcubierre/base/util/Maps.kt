package com.github.octaone.alcubierre.base.util

import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi

@AlcubierreInternalApi
fun <K, V> Map<K, V>.getNotNull(key: K): V =
    get(key) ?: throw NoSuchElementException("Key $key is missing in the map.")
