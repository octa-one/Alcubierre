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

package space.octaone.alcubierre.base.screen.extra

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
