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

package space.octaone.alcubierre.base.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.base.screen.internal.ScreenParceler

/**
 * State of a single stack.
 *
 * @param stack Screens stack. The last screen in the stack is the one the user sees.
 * Default reducer fills the queue based on the priority field.
 *
 * [equals] only uses [Screen.screenId] to compare screens.
 */
@Parcelize
public data class StackNavState<out S : Screen>(
    val stack: List<@WriteWith<ScreenParceler> S>
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StackNavState<*>

        if (stack.size != other.stack.size) return false
        for (i in stack.indices) {
            if (stack[i].screenId != other.stack[i].screenId) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = 1
        for (i in stack.indices) result = 31 * result + stack[i].screenId.hashCode()
        return result
    }

    public companion object {
        public val EMPTY: StackNavState<Nothing> = StackNavState(emptyList())
    }
}
