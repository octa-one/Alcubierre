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

import space.octaone.alcubierre.base.action.AnyNavAction
import space.octaone.alcubierre.base.reduce.LinkedNavReducer
import space.octaone.alcubierre.base.state.AnyRootNavState

/**
 * [space.octaone.alcubierre.base.reduce.NavReducer] for listening to stack selection.
 * Should be placed at the beginning of a reducers chain in order to observe changes made by the following reducers.
 *
 * @param onStackChanged Callback, invoked after selecting a new stack.
 */
public class StackChangedListenerReducer(
    private val onStackChanged: (from: Int, to: Int) -> Unit
): LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState {
        val newState = next.reduce(state, action)
        if (state.currentStackId != newState.currentStackId) {
            onStackChanged(state.currentStackId, newState.currentStackId)
        }
        return newState
    }
}
