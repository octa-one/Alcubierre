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

package space.octaone.alcubierre.owner

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import space.octaone.alcubierre.ComposeNavDriveOwner
import space.octaone.alcubierre.core.NavDriveOwner
import space.octaone.alcubierre.core.owner.AlcubierreNavDriveOwner
import space.octaone.alcubierre.core.reduce.NavReducer
import space.octaone.alcubierre.core.state.AnyRootNavState
import space.octaone.alcubierre.screen.ComposeDialog
import space.octaone.alcubierre.screen.ComposeScreen
import space.octaone.alcubierre.state.ComposeRootNavState

/**
 * Composable function that saves the state of [NavDriveOwner] with [rememberSaveable].
 *
 * Be aware: [reducer] and [initialState] are not [rememberSaveable] keys.
 * [AlcubierreNavDriveOwner] is only initialized once, and then there is no point
 * in recreating it when the keys change, since it stores the navigation state.
 *
 * In fact, it is not required to save [NavDriveOwner] in the Compose code.
 * It can be created somewhere in a DI container or root Activity,
 * and `SavedStateHandle` or `Activity::onSaveInstanceState` can be used to handle saving the state.
 */
@Composable
public fun rememberNavDriveOwner(
    reducer: NavReducer<AnyRootNavState>,
    initialState: ComposeRootNavState
) : ComposeNavDriveOwner =
    rememberSaveable(
        saver = Saver<AlcubierreNavDriveOwner<ComposeScreen, ComposeDialog>, Bundle>(
            save = { owner -> Bundle().also(owner::saveState) },
            restore = { bundle ->
                AlcubierreNavDriveOwner<ComposeScreen, ComposeDialog>().also { owner ->
                    owner.initialize(
                        reducer = reducer,
                        initialState = initialState
                    )
                    owner.restoreState(bundle)
                }
            }
        )
    ) {
        AlcubierreNavDriveOwner<ComposeScreen, ComposeDialog>().also { owner ->
            owner.initialize(
                reducer = reducer,
                initialState = initialState
            )
        }
    }
