package com.github.octaone.alcubierre.host

import android.os.Bundle
import com.github.octaone.alcubierre.AlcubierreHost
import com.github.octaone.alcubierre.action.NavigationAction
import com.github.octaone.alcubierre.action.applyState
import com.github.octaone.alcubierre.reduce.NavigationReducer
import com.github.octaone.alcubierre.render.AlcubierreRootNavigationRender
import com.github.octaone.alcubierre.state.RootNavigationState
import com.github.octaone.alcubierre.state.RootSavedState
import com.github.octaone.alcubierre.util.getParcelableCompat
import kotlin.properties.Delegates

class AlcubierreStandaloneHost : AlcubierreHost {

    private var isReadyToProcess = false
    private var isStatePending = false
    private var isStateSaved = false

    private var reducer: NavigationReducer<RootNavigationState> by Delegates.notNull()
    private var render: AlcubierreRootNavigationRender by Delegates.notNull()

    private var currentState: RootNavigationState = RootNavigationState.EMPTY
    override val state: RootNavigationState get() = currentState

    fun initialize(
        reducer: NavigationReducer<RootNavigationState>,
        render: AlcubierreRootNavigationRender,
        savedState: Bundle?,
        initialState: RootNavigationState
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

    override fun dispatch(action: NavigationAction) {
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
