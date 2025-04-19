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

package space.octaone.alcubierre.reduce

import space.octaone.alcubierre.action.Back
import space.octaone.alcubierre.action.BackTo
import space.octaone.alcubierre.action.BackToRoot
import space.octaone.alcubierre.action.Forward
import space.octaone.alcubierre.action.Replace
import space.octaone.alcubierre.action.ReplaceRoot
import space.octaone.alcubierre.core.action.AnyNavAction
import space.octaone.alcubierre.core.reduce.NavReducer
import space.octaone.alcubierre.core.screen.Screen
import space.octaone.alcubierre.core.state.AnyStackNavState
import space.octaone.alcubierre.util.optimizeReadOnlyList

/**
 * [NavReducer] responds for commands with specific stack
 */
public class ScreenStackNavReducer : NavReducer<AnyStackNavState> {

    override fun reduce(
        state: AnyStackNavState,
        action: AnyNavAction
    ): AnyStackNavState =
        when (action) {
            is Forward -> {
                state.modifyStack { addAll(action.screens) }
            }
            is Replace -> {
                state.modifyStack {
                    removeAt(lastIndex)
                    addAll(action.screens)
                }
            }
            is Back -> {
                if (state.stack.size > 1) {
                    state.modifyStack { removeAt(lastIndex) }
                } else {
                    state
                }
            }
            is BackTo -> {
                val i = state.stack.indexOfLast { it.screenId == action.screen.screenId }
                if (i != -1) {
                    state.modifyStack {
                        repeat(lastIndex - i) { removeAt(lastIndex) }
                        if (action.inclusive) removeAt(lastIndex)
                    }
                } else {
                    state
                }
            }
            is BackToRoot -> {
                state.modifyStack {
                    val root = getOrNull(0)
                    clear()
                    if (root != null) add(root)
                }
            }
            is ReplaceRoot -> {
                check(action.screens.isNotEmpty())
                state.copy(stack = action.screens)
            }
            else -> {
                state
            }
    }

    private inline fun AnyStackNavState.modifyStack(update: MutableList<Screen>.() -> Unit): AnyStackNavState =
        copy(stack = stack.toMutableList().apply(update).optimizeReadOnlyList())
}
