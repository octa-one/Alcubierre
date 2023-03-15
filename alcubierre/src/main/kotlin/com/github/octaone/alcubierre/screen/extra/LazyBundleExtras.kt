package com.github.octaone.alcubierre.screen.extra

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import java.io.Serializable

class LazyBundleExtras : ParcelableExtras {

    private val lazyBundle = lazy(LazyThreadSafetyMode.NONE) { Bundle() }
    private val bundle by lazyBundle

    override fun containsKey(key: String): Boolean =
        if (lazyBundle.isInitialized()) bundle.containsKey(key) else false

    override fun remove(key: String) {
        if (lazyBundle.isInitialized()) {
            bundle.remove(key)
        }
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

    override fun getString(key: String): String =
        requireNotNull(bundle.getString(key))

    override fun getInt(key: String): Int =
        bundle.getInt(key)

    override fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>): T =
        requireNotNull(
            BundleCompat.getParcelable(bundle, key, clazz)
        )

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override fun <T : Serializable> getSerializable(key: String, clazz: Class<T>): T =
        requireNotNull(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(key, clazz)
            } else {
                bundle.getSerializable(key) as T
            }
        )

    override fun saveExtras(): Bundle? =
        if (lazyBundle.isInitialized()) bundle else null

    override fun restoreExtras(bundle: Bundle) {
        bundle.putAll(bundle)
    }
}
