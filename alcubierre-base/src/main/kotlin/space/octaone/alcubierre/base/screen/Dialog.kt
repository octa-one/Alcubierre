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
import androidx.annotation.IntRange
import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.base.screen.extra.ExtrasContainer
import java.util.UUID

/**
 * Abstraction for describing dialog destination for navigation.
 *
 * The main difference from [Screen] is that the [Dialog] is shown outside the stack on top of any content.
 * And also there can only be one shown dialog at a time.
 * The remaining added dialogs will be queued according to [priority].
 *
 * Priority handling depends on the implementation of the reducer.
 * The default implementation assumes that the visible dialog has the highest priority.
 */
public abstract class Dialog : Parcelable, Comparable<Dialog>, ExtrasContainer {

    @get:IntRange(0, Long.MAX_VALUE)
    public abstract val priority: Int

    public var isShowing: Boolean = false
        @AlcubierreInternalApi set

    public var dialogId: String = UUID.randomUUID().toString()
        @AlcubierreInternalApi set

    override fun compareTo(other: Dialog): Int = priority.compareTo(other.priority)
}
