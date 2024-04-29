package com.github.octaone.alcubierre.screen.extra

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * Parcelable object storage interface.
 */
public interface ParcelableExtras {

    public fun containsKey(key: String): Boolean

    public fun remove(key: String)

    public fun putBoolean(key: String, value: Boolean)

    public fun putString(key: String, value: String)

    public fun putInt(key: String, value: Int)

    public fun putParcelable(key: String, value: Parcelable)

    public fun putSerializable(key: String, value: Serializable)

    public fun getBoolean(key: String): Boolean

    public fun getString(key: String): String?

    public fun getInt(key: String): Int

    public fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>): T?

    public fun <T : Serializable> getSerializable(key: String, clazz: Class<T>): T?

    public fun save(): Bundle

    public fun restore(bundle: Bundle)
}
