package com.github.octaone.alcubierre.util

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Bundle.requireParcelableCompat(key: String) : T =
    getParcelableCompat(key, T::class.java).let(::requireNotNull)

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String) : T? =
    getParcelableCompat(key, T::class.java)

inline fun <reified T : Parcelable> Bundle.requireParcelableArrayCompat(key: String) : Array<T>? =
    getParcelableArrayCompat(key, T::class.java)

inline fun <reified T : Parcelable> Bundle.getParcelableArrayCompat(key: String) : Array<T>? =
    getParcelableArrayCompat(key, T::class.java)

@PublishedApi
@Suppress("DEPRECATION")
internal fun <T : Parcelable> Bundle.getParcelableCompat(key: String, clazz: Class<T>) : T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, clazz)
    } else {
        getParcelable(key)
    }

@PublishedApi
@Suppress("DEPRECATION", "UNCHECKED_CAST")
internal fun <T : Parcelable> Bundle.getParcelableArrayCompat(key: String, clazz: Class<T>) : Array<T>? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArray(key, clazz)
    } else {
        getParcelableArray(key) as Array<T>?
    }