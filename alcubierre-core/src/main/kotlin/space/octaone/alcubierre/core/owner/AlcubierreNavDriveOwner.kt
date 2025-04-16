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

package space.octaone.alcubierre.core.owner

import android.os.Bundle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import space.octaone.alcubierre.core.NavDriveOwner
import space.octaone.alcubierre.core.action.NavAction
import space.octaone.alcubierre.core.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.core.reduce.NavReducer
import space.octaone.alcubierre.core.screen.Dialog
import space.octaone.alcubierre.core.screen.Screen
import space.octaone.alcubierre.core.state.AnyRootNavState
import space.octaone.alcubierre.core.state.DialogNavState
import space.octaone.alcubierre.core.state.RootNavState
import space.octaone.alcubierre.core.util.getParcelableCompat
import kotlin.reflect.KClass

/**
 * Default implementation of [NavDriveOwner].
 * Holds the current [state],
 * contains logic for saving and restoring it, and transforms it using the [reducer] chain.
 */
public class AlcubierreNavDriveOwner<S : Screen, D : Dialog> : NavDriveOwner<S, D> {

    private lateinit var reducer: NavReducer<AnyRootNavState>

    private val _stateFlow = MutableStateFlow<RootNavState<S, D>>(RootNavState.EMPTY)
    override val stateFlow: StateFlow<RootNavState<S, D>> get() = _stateFlow

    override val state: RootNavState<S, D> get() = _stateFlow.value

    override fun initialize(
        reducer: NavReducer<AnyRootNavState>,
        initialState: RootNavState<S, D>,
        extras: Map<KClass<*>, Any> // Does not require any extras
    ) {
        this.reducer = reducer
        _stateFlow.value = initialState
    }

    override fun restoreState(savedState: Bundle?) {
        check(::reducer.isInitialized) { "NavDriveOwner is not initialized" }
        savedState?.getParcelableCompat<RootNavState<S, D>>(BUNDLE_KEY_STATE)?.let { restoredState ->
            _stateFlow.value = restoredState
        }
    }

    override fun saveState(outState: Bundle) {
        outState.putParcelable(BUNDLE_KEY_STATE, state)
    }

    @Suppress("UNCHECKED_CAST")
    override fun dispatch(action: NavAction<S, D>) {
        _stateFlow.value = reducer.reduce(state, action) as RootNavState<S, D>
    }

    override fun requestDismissDialog() {
        if (state.currentDialog == null) return

        val newDialogState = if (state.dialogState.queue.size <= 1) {
            DialogNavState.EMPTY
        } else {
            val mutableQueue = state.dialogState.queue.toMutableList()
            mutableQueue.removeAt(0)
            DialogNavState(mutableQueue.toList())
        }

        _stateFlow.value = state.copy(dialogState = newDialogState)
    }
}

private const val BUNDLE_KEY_STATE = "alc_state"
