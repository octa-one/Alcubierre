package com.github.octaone.alcubierre.owner

import android.os.Bundle
import com.github.octaone.alcubierre.NavDriveOwner
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.reduce.NavReducer
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.isShowing
import com.github.octaone.alcubierre.state.AnyRootNavState
import com.github.octaone.alcubierre.state.RootNavState
import com.github.octaone.alcubierre.util.getParcelableCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

class AlcubierreNavDriveOwner<S : Screen, D : Dialog> : NavDriveOwner<S, D> {

    private lateinit var reducer: NavReducer<AnyRootNavState>

    private val _stateFlow = MutableStateFlow<RootNavState<S, D>>(RootNavState.EMPTY)
    override val stateFlow: StateFlow<RootNavState<S, D>> get() = _stateFlow

    override val state: RootNavState<S, D> get() = _stateFlow.value

    override fun initialize(
        reducer: NavReducer<AnyRootNavState>,
        initialState: RootNavState<S, D>,
        extras: Map<KClass<*>, Any>
    ) {
        this.reducer = reducer
        _stateFlow.value = initialState
    }

    override fun restoreState(savedState: Bundle?) {
        check(::reducer.isInitialized) { "NavDriveOwner was not initialized" }
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
        val tempState = state
        tempState.dialogState.queue.firstOrNull()?.isShowing = false
        val newState = tempState.copy(dialogState = tempState.dialogState.copy(queue = tempState.dialogState.queue.drop(1)))

        _stateFlow.value = newState
    }
}

private const val BUNDLE_KEY_STATE = "alc_state"
