package com.github.octaone.alcubierre.util

import kotlin.reflect.KClass

fun <K, V> Map<K, V>.getNotNull(key: K): V =
    get(key) ?: throw NoSuchElementException("Key $key is missing in the map.")

inline fun <reified V : Any> Map<KClass<*>, *>.getAndCast(): V? = get(V::class) as? V
