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

package space.octaone.alcubierre.reducer

import android.net.Uri
import space.octaone.alcubierre.action.DeeplinkForward
import space.octaone.alcubierre.action.Forward
import space.octaone.alcubierre.action.ShowDialog
import space.octaone.alcubierre.core.action.AnyNavAction
import space.octaone.alcubierre.core.reduce.LinkedNavReducer
import space.octaone.alcubierre.core.screen.Dialog
import space.octaone.alcubierre.core.screen.Screen
import space.octaone.alcubierre.core.state.AnyRootNavState
import space.octaone.alcubierre.condition.ConditionalTarget
import space.octaone.alcubierre.condition.action.ResolveCondition
import space.octaone.alcubierre.deeplink.DeeplinkResolver
import space.octaone.alcubierre.deeplink.DefaultDeeplinkResolver

/**
 * Reducer for handling [DeeplinkForward] actions.
 * Uses [DeeplinkResolver] to create actual navigation targets.
 * Support 3 targets:
 * Resolved [Screen] is converted to a [Forward] action.
 * Resolved [Dialog] is converted to a [ShowDialog] action.
 * Resolved [ConditionalTarget] is converted to a [ResolveCondition] action for further processing.
 *
 * Be aware: resolved action will be reduced from the head or chain reducers.
 * So it doesn't matter if [DeeplinkReducer] is at the end or the beginning of the chain.
 * But it's better to place it at the beginning to minimize unnecessary reduce calls.
 *
 * @param resolver [DeeplinkResolver] with all known deeplink, can be created with [DefaultDeeplinkResolver].
 * @param onResolveFailed Callback to handle unknown deeplinks that cannot be resolved by [resolver].
 */
public class DeeplinkReducer(
    private val resolver: DeeplinkResolver,
    private val onResolveFailed: (Uri) -> Unit
) : LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState =
        when (action) {
            is DeeplinkForward -> {
                val resolvedAction = resolver.resolve(action.deeplink)
                    .map { resolved ->
                        when (resolved) {
                            is Screen -> Forward(listOf(resolved))
                            is Dialog -> ShowDialog(resolved)
                            is ConditionalTarget -> ResolveCondition(resolved)
                            else -> null
                        }
                    }
                    .getOrNull()

                if (resolvedAction != null) {
                    head.reduce(state, resolvedAction)
                } else {
                    onResolveFailed(action.deeplink)
                    state
                }
            }
            else -> {
                next.reduce(state, action)
            }
        }
}
