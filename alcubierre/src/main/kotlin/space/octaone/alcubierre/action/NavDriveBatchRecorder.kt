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

package space.octaone.alcubierre.action

import space.octaone.alcubierre.base.NavDrive
import space.octaone.alcubierre.base.action.NavAction
import space.octaone.alcubierre.base.screen.Dialog
import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.base.state.RootNavState

/**
 * Utility class for recording multiple [space.octaone.alcubierre.base.action.NavAction]s and dispatching them simultaneously.
 */
internal class NavDriveBatchRecorder<S : Screen, D : Dialog> (
    initialState: RootNavState<S, D>
) : NavDrive<S, D> {

    override val state: RootNavState<S, D> = initialState

    val actions = mutableListOf<NavAction<S, D>>()

    override fun dispatch(action: NavAction<S, D>) {
        actions.add(action)
    }
}
