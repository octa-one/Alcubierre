package com.github.octaone.alcubierre.host

import android.os.Bundle
import com.github.octaone.alcubierre.NavDriveOwner
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.action.applyState
import com.github.octaone.alcubierre.reduce.NavReducer
import com.github.octaone.alcubierre.render.AlcubierreRootNavRender
import com.github.octaone.alcubierre.state.RootNavState
import com.github.octaone.alcubierre.state.RootSavedState
import com.github.octaone.alcubierre.util.getParcelableCompat
import kotlin.properties.Delegates

class AlcubierreNavDriveOwner : NavDriveOwner {

    private var isReadyToProcess = false
    private var isStatePending = false
    private var isStateSaved = false

    private var reducer: NavReducer<RootNavState> by Delegates.notNull()
    private var render: AlcubierreRootNavRender by Delegates.notNull()

    private var currentState: RootNavState = RootNavState.EMPTY
    override val state: RootNavState get() = currentState

    fun initialize(
        reducer: NavReducer<RootNavState>,
        render: AlcubierreRootNavRender,
        savedState: Bundle?,
        initialState: RootNavState
    ) {
        this.reducer = reducer
        this.render = render
        this.render.setOnDialogDismissed(::onDialogDismissed)

        isStateSaved = false

        savedState?.getParcelableCompat<RootSavedState>(KEY_STATE)?.let { restored ->
            this.render.restoreState(restored)
            currentState = restored.state
        } ?: run {
            applyState(initialState)
        }
    }

    override fun onResume() {
        isReadyToProcess = true
        if (isStatePending) {
            isStatePending = false
            applyState(state)
        }
    }

    override fun onPause() {
        isReadyToProcess = false
    }

    override fun saveState(outState: Bundle) {
        isStateSaved = true
        outState.putParcelable(KEY_STATE, render.saveState())
    }

    override fun dispatch(action: NavAction) {
        if (isStateSaved) return

        currentState = reducer.reduce(state, action)
        if (isReadyToProcess) {
            render.render(currentState)
        } else {
            isStatePending = true
        }
    }

    private fun onDialogDismissed() {
        currentState = currentState.copy(dialog = null)
    }
}

private const val KEY_STATE = "state"
