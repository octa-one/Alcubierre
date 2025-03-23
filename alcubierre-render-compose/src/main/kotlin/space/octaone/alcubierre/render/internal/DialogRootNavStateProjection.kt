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

package space.octaone.alcubierre.render.internal

import androidx.compose.runtime.Immutable
import space.octaone.alcubierre.state.ComposeRootNavState

/**
 * Compose runtime checks if a function can be skipped by comparing its arguments.
 * This class will ignore any changes made to the screen stacks if the dialog remains unchanged.
 */
@Immutable
internal class DialogRootNavStateProjection(
    val state: ComposeRootNavState
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DialogRootNavStateProjection

        return state.currentDialog?.dialogId == other.state.currentDialog?.dialogId
    }

    // The hashCode/equals contract is not respected, but in this case it doesn't need to be.
    override fun hashCode(): Int = 0
}
