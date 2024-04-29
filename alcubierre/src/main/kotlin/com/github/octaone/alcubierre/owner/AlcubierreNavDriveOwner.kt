@file:OptIn(AlcubierreInternalApi::class)

package com.github.octaone.alcubierre.owner

import android.os.Bundle
import com.github.octaone.alcubierre.NavDriveOwner
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.base.util.getParcelableCompat
import com.github.octaone.alcubierre.reduce.NavReducer
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.AnyRootNavState
import com.github.octaone.alcubierre.state.DialogNavState
import com.github.octaone.alcubierre.state.RootNavState
import com.github.octaone.alcubierre.util.optimizeReadOnlyList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            DialogNavState(mutableQueue.optimizeReadOnlyList())
        }

        _stateFlow.value = state.copy(dialogState = newDialogState)
    }
}

private const val BUNDLE_KEY_STATE = "alc_state"
