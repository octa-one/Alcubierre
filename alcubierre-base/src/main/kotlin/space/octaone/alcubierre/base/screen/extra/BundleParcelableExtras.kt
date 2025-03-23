/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(AlcubierreInternalApi::class)

package space.octaone.alcubierre.base.screen.extra

import android.os.Bundle
import android.os.Parcelable
import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.base.util.getParcelableCompat
import space.octaone.alcubierre.base.util.getSerializableCompat
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
