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

package space.octaone.alcubierre.reduce

import space.octaone.alcubierre.action.ApplyState
import space.octaone.alcubierre.action.Back
import space.octaone.alcubierre.action.BackTo
import space.octaone.alcubierre.action.BackToRoot
import space.octaone.alcubierre.action.ClearStack
import space.octaone.alcubierre.action.Forward
import space.octaone.alcubierre.action.NewStack
import space.octaone.alcubierre.action.Replace
import space.octaone.alcubierre.action.ReplaceRoot
import space.octaone.alcubierre.action.SelectStack
import space.octaone.alcubierre.core.action.AnyNavAction
import space.octaone.alcubierre.core.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.core.reduce.LinkedNavReducer
import space.octaone.alcubierre.core.reduce.NavReducer
import space.octaone.alcubierre.core.state.AnyRootNavState
import space.octaone.alcubierre.core.state.AnyStackNavState
import space.octaone.alcubierre.core.state.RootNavState
import space.octaone.alcubierre.core.state.StackNavState
import space.octaone.alcubierre.core.util.getNotNull
import space.octaone.alcubierre.util.optimizeReadOnlyMap

/**
 * [NavReducer] for screen specific actions. Responsible for [RootNavState].
 * For actions associated with a particular stack, forwards them to [stackReducer],
 * then updates [RootNavState] with the updated [StackNavState] from [stackReducer].
 *
 * [NewStack] action creates a new stack in [RootNavState.stackStates].
 * [SelectStack] action changes value of [RootNavState.currentStackId].
 * Make sure that [SelectStack.stackId] exists in [RootNavState].
 * [ClearStack] action removes stack from [RootNavState].
 * Make sure you do not remove current [RootNavState.currentStackId] stack.
 * [ApplyState] action returns a completely new state from [ApplyState.state].
 *
 * @param stackReducer [NavReducer] that can reduce [StackNavState].
 */
public class ScreenRootNavReducer(
    private val stackReducer: NavReducer<AnyStackNavState> = ScreenStackNavReducer()
) : LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState = when (action) {
        is Forward, is Back, is Replace, is BackToRoot, is ReplaceRoot, is BackTo -> {
            state.modifyStack(state.currentStackId) { stackReducer.reduce(this, action) }
        }
        is NewStack -> {
            state.modifyStacks { put(action.stackId, StackNavState(action.screens)) }
        }
        is SelectStack -> {
            if (state.currentStackId == action.stackId) {
                state
            } else {
                check(state.stackStates.containsKey(action.stackId))
                state.copy(currentStackId = action.stackId)
            }
        }
        is ClearStack -> {
            if (state.stackStates.containsKey(action.stackId)) {
                check(state.currentStackId != action.stackId)
                state.modifyStacks { remove(action.stackId) }
            } else {
                state
            }
        }
        is ApplyState -> {
            action.state
        }
        else -> {
            state
        }
    }

    private inline fun AnyRootNavState.modifyStack(id: Int, update: AnyStackNavState.() -> AnyStackNavState): AnyRootNavState =
        modifyStacks { put(id, stackStates.getNotNull(id).update()) }

    private inline fun AnyRootNavState.modifyStacks(update: MutableMap<Int, AnyStackNavState>.() -> Unit): AnyRootNavState =
        copy(stackStates = HashMap(stackStates).apply(update).optimizeReadOnlyMap())
}
