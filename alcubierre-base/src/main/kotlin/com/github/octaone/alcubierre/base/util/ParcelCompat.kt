@file:Suppress("DEPRECATION")

package com.github.octaone.alcubierre.base.util

import android.os.Build
import android.os.Parcel
import android.os.Parcelable

internal fun Parcel.writeBooleanCompat(flag: Boolean) {
    writeInt(if (flag) 1 else 0)
}

internal fun Parcel.readBooleanCompat(): Boolean =
    readInt() != 0

internal fun <T : Parcelable> Parcel.readParcelableCompat(loader: ClassLoader?, clazz: Class<T>): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        readParcelable(loader, clazz)
    } else {
        val parcelable = readParcelable<T>(loader)
        if (clazz.isInstance(parcelable)) parcelable as T else null
    }
