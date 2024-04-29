@file:OptIn(AlcubierreInternalApi::class)

package com.github.octaone.alcubierre.screen.extra

import android.os.Bundle
import android.os.Parcelable
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.base.util.getParcelableCompat
import com.github.octaone.alcubierre.base.util.getSerializableCompat
import java.io.Serializable

/**
 * A [ParcelableExtras] implementation based on [Bundle].
 */
internal class BundleParcelableExtras : ParcelableExtras {

    private val bundle = Bundle()

    override fun containsKey(key: String): Boolean =
        bundle.containsKey(key)

    override fun remove(key: String) {
        bundle.remove(key)
    }

    override fun putBoolean(key: String, value: Boolean) {
        bundle.putBoolean(key, value)
    }

    override fun putString(key: String, value: String) {
        bundle.putString(key, value)
    }

    override fun putInt(key: String, value: Int) {
        bundle.putInt(key, value)
    }

    override fun putParcelable(key: String, value: Parcelable) {
        bundle.putParcelable(key, value)
    }

    override fun putSerializable(key: String, value: Serializable) {
        bundle.putSerializable(key, value)
    }

    override fun getBoolean(key: String): Boolean =
        bundle.getBoolean(key)

    override fun getString(key: String): String? =
        bundle.getString(key)

    override fun getInt(key: String): Int =
        bundle.getInt(key)

    override fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>): T? =
        bundle.getParcelableCompat(key, clazz)

    override fun <T : Serializable> getSerializable(key: String, clazz: Class<T>): T? =
        bundle.getSerializableCompat(key, clazz)

    override fun save(): Bundle = bundle

    override fun restore(bundle: Bundle) {
        this.bundle.putAll(bundle)
    }
}