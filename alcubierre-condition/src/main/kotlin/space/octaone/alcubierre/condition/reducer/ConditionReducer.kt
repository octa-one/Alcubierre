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

package space.octaone.alcubierre.condition.reducer

import space.octaone.alcubierre.core.action.AnyNavAction
import space.octaone.alcubierre.core.reduce.LinkedNavReducer
import space.octaone.alcubierre.core.state.AnyRootNavState
import space.octaone.alcubierre.condition.NavCondition
import space.octaone.alcubierre.condition.NavConditionFactory
import space.octaone.alcubierre.condition.action.ResolveCondition

/**
 * Reducer for handling [ResolveCondition] actions.
 * Uses [NavConditionFactory] to create a [NavCondition]
 * and calls [NavCondition.resolve] to get the final action to be performed.
 *
 * Be aware: resolved action will be reduced from the head or chain reducers.
 * So it doesn't matter if [ConditionReducer] is at the end or the beginning of the chain.
 * But it's better to place it at the beginning to minimize unnecessary reduce calls.
 */
public class ConditionReducer(
    private val conditionFactory: NavConditionFactory
) : LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState = when (action) {
        is ResolveCondition -> {
            conditionFactory.create(action.conditionalTarget)
                .resolve(action.conditionalTarget, state)
                ?.let { head.reduce(state, it) }
                ?: state
        }
        else -> {
            next.reduce(state, action)
        }
    }
}
