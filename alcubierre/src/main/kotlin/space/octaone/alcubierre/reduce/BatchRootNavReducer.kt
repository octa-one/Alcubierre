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

import space.octaone.alcubierre.action.Batch
import space.octaone.alcubierre.core.action.AnyNavAction
import space.octaone.alcubierre.core.reduce.LinkedNavReducer
import space.octaone.alcubierre.core.state.AnyRootNavState
import space.octaone.alcubierre.core.reduce.NavReducer

/**
 * [NavReducer] for [Batch] action.
 * Reduce every recorded action from the head of the chain. Thus, Batch actions can be nested.
 */
public class BatchRootNavReducer : LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState =
        when (action) {
            is Batch -> {
                action.actions.fold(state) { foldState, foldAction -> head.reduce(foldState, foldAction) }
            }
            else -> {
                next.reduce(state, action)
            }
        }
}
