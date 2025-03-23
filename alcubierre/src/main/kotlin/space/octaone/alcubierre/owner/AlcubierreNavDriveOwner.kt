@file:OptIn(AlcubierreInternalApi::class)

package space.octaone.alcubierre.owner

import android.os.Bundle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import space.octaone.alcubierre.base.NavDriveOwner
import space.octaone.alcubierre.base.action.NavAction
import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.base.reduce.NavReducer
import space.octaone.alcubierre.base.screen.Dialog
import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.base.state.AnyRootNavState
import space.octaone.alcubierre.base.state.DialogNavState
import space.octaone.alcubierre.base.state.RootNavState
import space.octaone.alcubierre.base.util.getParcelableCompat
import space.octaone.alcubierre.util.optimizeReadOnlyList
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
