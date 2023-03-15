package com.github.octaone.alcubierre.util

fun <K, V> Map<K, V>.getNotNull(key: K): V =
    get(key) ?: throw NoSuchElementException("Key $key is missing in the map.")

fun <K, V> Map<K, V>.toHashMap(): HashMap<K, V> = HashMap(this)
