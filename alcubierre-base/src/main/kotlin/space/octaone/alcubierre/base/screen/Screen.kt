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

package space.octaone.alcubierre.base.screen

import android.os.Parcelable
import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.base.screen.extra.ExtrasContainer
import java.util.UUID

/**
 * Abstraction for describing a destination for navigation.
 *
 * Be aware:
 * The library only uses random [screenId] to compare screens.
 * This makes possible to add multiple identical screens to the backstack.
 *
 * Be careful when using the `object` modifier for screens.
 * Adding such object to the backstack multiple times may cause errors.
 */
public abstract class Screen : Parcelable, ExtrasContainer {

    public var screenId: String = UUID.randomUUID().toString()
        @AlcubierreInternalApi set
}
