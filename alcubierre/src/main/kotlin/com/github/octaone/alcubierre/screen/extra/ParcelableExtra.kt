package com.github.octaone.alcubierre.screen.extra

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

interface ParcelableExtras {

    fun containsKey(key: String): Boolean

    fun remove(key: String)

    fun putBoolean(key: String, value: Boolean)

    fun putString(key: String, value: String)

    fun putInt(key: String, value: Int)

    fun putParcelable(key: String, value: Parcelable)

    fun putSerializable(key: String, value: Serializable)

    fun getBoolean(key: String): Boolean

    fun getString(key: String): String

    fun getInt(key: String): Int

    fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>): T

    fun <T : Serializable> getSerializable(key: String, clazz: Class<T>): T

    fun saveExtras(): Bundle?

    fun restoreExtras(bundle: Bundle)
}
