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

package space.octaone.alcubierre.core

import android.os.Bundle
import kotlinx.coroutines.flow.StateFlow
import space.octaone.alcubierre.core.action.NavAction
import space.octaone.alcubierre.core.reduce.NavReducer
import space.octaone.alcubierre.core.screen.Dialog
import space.octaone.alcubierre.core.screen.Screen
import space.octaone.alcubierre.core.state.AnyRootNavState
import space.octaone.alcubierre.core.state.RootNavState
import kotlin.reflect.KClass

/**
 * Base interface of navigation entrypoint.
 *
 * Library consists of 3 base components:
 *  - [NavReducer]: transforms [RootNavState] according to [NavAction].
 *  - Render: translate commands to android framework (e.g. FragmentManager) for rendering specific [RootNavState].
 *    Does not have a base class, as implementation is highly dependent on the UI framework (e.g. Compose or FragmentManager).
 *  - [NavDriveOwner]: class that stores the current state. It is an entry point for dispatching navigation commands.
 *
 * [state] - current navigation state
 * [dispatch] - method for [NavAction] application
 */
public interface NavDrive<S : Screen, D : Dialog> {

    /**
     * Current navigation state.
     */
    public val state: RootNavState<S, D>

    /**
     * Dispatch new navigation action.
     *
     * A few notes on the behavior of the default implementation:
     * * [dispatch] should be called on the main thread.
     * * [dispatch] can be called any time regardless of the application lifecycle.
     * This means that if actions are dispatched after onSavedInstanceState,
     * they will not be saved in case of process death. (similar to the behavior of commitAllowStateLoss).
     * This is usually not a problem if the navigation events come from the UI.
     * But you can alter this behavior by implementing custom lifecycle-aware NavDriveOwner
     * or NavDrive extensions, eg 'forwardDisallowStateLoss'.
     */
    public fun dispatch(action: NavAction<S, D>)
}

/**
 * Base interface of navigation owner.
 */
public interface NavDriveOwner<S : Screen, D : Dialog> : NavDrive<S, D> {

    /**
     * Observable navigation state.
     */
    public val stateFlow: StateFlow<RootNavState<S, D>>

    /**
     * NavDriveOwner initialization. Should be called before any [dispatch] methods.
     * @param reducer Head of the reducers linked list. All actions will be passed to this reducer.
     * @param initialState Initial state of navigation. Will be shown if there is no restored state.
     * @param extras Map with additional parameters. Supported parameters depend on specific implementation.
     */
    public fun initialize(
        reducer: NavReducer<AnyRootNavState>,
        initialState: RootNavState<S, D>,
        extras: Map<KClass<*>, Any> = emptyMap()
    )

    /**
     * Should not be called directly by user's code.
     * This method will be invoked when the dialog is dismissed by user interaction and not by changing NavDrive state.
     * Implementations should update their state without any interceptions.
     */
    public fun requestDismissDialog()

    /**
     * Saving current NavDrive state.
     */
    public fun saveState(outState: Bundle)

    /**
     * Restoring saved NavDrive state.
     */
    public fun restoreState(savedState: Bundle?)
}
