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

@file:Suppress("DEPRECATION")

package space.octaone.alcubierre.base.util

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
