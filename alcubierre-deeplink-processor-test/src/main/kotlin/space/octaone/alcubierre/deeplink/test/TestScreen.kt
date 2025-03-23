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

package space.octaone.alcubierre.deeplink.test

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.base.screen.extra.ExtrasContainer
import space.octaone.alcubierre.base.screen.extra.ParcelableExtras

@SuppressLint("ParcelCreator", "NonDataScreenClassRule")
open class TestScreen : Screen(), Parcelable by ParcelableStub, ExtrasContainer by ExtrasContainerStub

private object ExtrasContainerStub : ExtrasContainer {

    override val extras: ParcelableExtras get() = throw UnsupportedOperationException()

    override fun hasExtras(): Boolean = false
}

@SuppressLint("ParcelCreator")
private object ParcelableStub : Parcelable {
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) = Unit
}
