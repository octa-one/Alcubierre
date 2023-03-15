package com.github.octaone.alcubierre.util

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String) : T? =
    BundleCompat.getParcelable(this, key, T::class.java)

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Parcelable> Bundle.getParcelableArrayCompat(key: String) : Array<T>? =
    BundleCompat.getParcelableArray(this, key, T::class.java) as Array<T>?
