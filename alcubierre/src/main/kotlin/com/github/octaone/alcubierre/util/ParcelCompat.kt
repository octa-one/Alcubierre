@file:Suppress("DEPRECATION")

package com.github.octaone.alcubierre.util

import android.os.Build
import android.os.Parcel
import android.os.Parcelable

internal fun <T : Parcelable> Parcel.readParcelableCompat(loader: ClassLoader?, clazz: Class<T>) : T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        readParcelable(loader, clazz)
    } else {
        val parcelable = readParcelable<T>(loader)
        if (clazz.isInstance(parcelable)) parcelable as T else null
    }

internal inline fun <reified T : Parcelable> Parcel.readParcelableCompat(loader: ClassLoader?) : T? =
    readParcelableCompat(loader, T::class.java)
