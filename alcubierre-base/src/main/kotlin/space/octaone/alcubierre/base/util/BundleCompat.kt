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

@file:Suppress("UNCHECKED_CAST", "DEPRECATION")

package space.octaone.alcubierre.base.util

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import java.io.Serializable
import java.lang.reflect.Array as ReflectArray

@AlcubierreInternalApi
public fun <T : Parcelable> Bundle.getParcelableCompat(key: String, clazz: Class<T>) : T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        getParcelable(key, clazz)
    } else {
        val parcelable = getParcelable<T>(key)
        if (clazz.isInstance(parcelable)) parcelable as T else null
    }

@AlcubierreInternalApi
public inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String) : T? =
    getParcelableCompat(key, T::class.java)

@AlcubierreInternalApi
public fun <T : Parcelable> Bundle.getParcelableArrayCompat(key: String, clazz: Class<T>) : Array<T>? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        getParcelableArray(key, clazz)
    } else {
        getParcelableArray(key)?.let { rawArray ->
            val array = ReflectArray.newInstance(clazz, rawArray.size)
            System.arraycopy(rawArray, 0, array, 0, rawArray.size)
            array as Array<T>
        }
    }

@AlcubierreInternalApi
public inline fun <reified T : Parcelable> Bundle.getParcelableArrayCompat(key: String) : Array<T>? =
    getParcelableArrayCompat(key, T::class.java)

@AlcubierreInternalApi
public fun <T : Serializable> Bundle.getSerializableCompat(key: String, clazz: Class<T>) : T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        getSerializable(key, clazz)
    } else {
        val serializable = getSerializable(key)
        if (clazz.isInstance(serializable)) serializable as T else null
    }

@AlcubierreInternalApi
public inline fun <reified T : Serializable> Bundle.getSerializableCompat(key: String) : T? =
    getSerializableCompat(key, T::class.java)
