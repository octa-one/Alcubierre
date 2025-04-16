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

package space.octaone.alcubierre.condition.action

import space.octaone.alcubierre.core.NavDrive
import space.octaone.alcubierre.core.action.NavAction
import space.octaone.alcubierre.core.screen.Dialog
import space.octaone.alcubierre.core.screen.Screen
import space.octaone.alcubierre.condition.ConditionalTarget
import space.octaone.alcubierre.condition.reducer.ConditionReducer

/**
 * Action to resolve a conditional target.
 * @see ConditionalTarget
 * @see ConditionReducer
 */
public data class ResolveCondition<S : Screen, D : Dialog>(val conditionalTarget: ConditionalTarget): NavAction<S, D>

/**
 * Extension to dispatch action [ResolveCondition].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.resolveCondition(conditionalTarget: ConditionalTarget): Unit =
    dispatch(ResolveCondition(conditionalTarget))
