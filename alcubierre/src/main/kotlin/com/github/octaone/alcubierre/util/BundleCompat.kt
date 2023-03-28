@file:Suppress("UNCHECKED_CAST", "DEPRECATION")

package com.github.octaone.alcubierre.util

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import java.io.Serializable

fun <T : Parcelable> Bundle.getParcelableCompat(key: String, clazz: Class<T>) : T? =
    BundleCompat.getParcelable(this, key, clazz)

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String) : T? =
    getParcelableCompat(key, T::class.java)

fun <T : Parcelable> Bundle.getParcelableArrayCompat(key: String, clazz: Class<T>) : Array<T>? =
    BundleCompat.getParcelableArray(this, key, clazz) as? Array<T>

inline fun <reified T : Parcelable> Bundle.getParcelableArrayCompat(key: String) : Array<T>? =
    getParcelableArrayCompat(key, T::class.java)

fun <T : Serializable> Bundle.getSerializableCompat(key: String, clazz: Class<T>) : T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        getSerializable(key) as? T
    }

inline fun <reified T : Serializable> Bundle.getSerializableCompat(key: String) : T? =
    getSerializableCompat(key, T::class.java)
