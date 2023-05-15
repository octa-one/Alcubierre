package com.github.octaone.alcubierre.owner

import android.os.Bundle
import com.github.octaone.alcubierre.NavDriveOwner
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.reduce.NavReducer
import com.github.octaone.alcubierre.screen.isShowing
import com.github.octaone.alcubierre.state.RootNavState
import com.github.octaone.alcubierre.util.getParcelableCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

class AlcubierreNavDriveOwner : NavDriveOwner {

    private lateinit var reducer: NavReducer<RootNavState>

    private val _stateFlow = MutableStateFlow(RootNavState.EMPTY)
    override val stateFlow: StateFlow<RootNavState> get() = _stateFlow

    override val state: RootNavState get() = _stateFlow.value

    override fun initialize(
        reducer: NavReducer<RootNavState>,
        initialState: RootNavState,
        extras: Map<KClass<*>, Any>
    ) {
        this.reducer = reducer
        _stateFlow.value = initialState
    }

    override fun restoreState(savedState: Bundle?) {
        check(::reducer.isInitialized) { "NavDriveOwner was not initialized" }
        savedState?.getParcelableCompat<RootNavState>(BUNDLE_KEY_STATE)?.let { restoredState ->
            _stateFlow.value = restoredState
        }
    }

    override fun saveState(outState: Bundle) {
        outState.putParcelable(BUNDLE_KEY_STATE, state)
    }

    override fun dispatch(action: NavAction) {
        _stateFlow.value = reducer.reduce(state, action)
    }

    override fun requestDismissDialog() {
        val tempState = state
        tempState.dialogState.queue.firstOrNull()?.isShowing = false
        val newState = tempState.copy(dialogState = tempState.dialogState.copy(queue = tempState.dialogState.queue.drop(1)))

        _stateFlow.value = newState
    }
}

private const val BUNDLE_KEY_STATE = "alc_state"
