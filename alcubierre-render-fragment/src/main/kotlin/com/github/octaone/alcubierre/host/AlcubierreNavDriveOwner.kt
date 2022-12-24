package com.github.octaone.alcubierre.host

import android.os.Bundle
import com.github.octaone.alcubierre.NavDriveOwner
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.action.applyState
import com.github.octaone.alcubierre.reduce.NavReducer
import com.github.octaone.alcubierre.render.AlcubierreRootNavRender
import com.github.octaone.alcubierre.state.DialogNavState
import com.github.octaone.alcubierre.state.RootNavState
import com.github.octaone.alcubierre.util.getParcelableCompat
import kotlin.properties.Delegates

class AlcubierreNavDriveOwner : NavDriveOwner {

    private var isReadyToProcess = false
    private var isStatePending = false

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

        savedState?.getParcelableCompat<RootNavState>(BUNDLE_KEY_STATE)?.let { restoredState ->
            currentState = restoredState
            this.render.restoreState(savedState)
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
        outState.putParcelable(BUNDLE_KEY_STATE, currentState)
        render.saveState(outState)
    }

    override fun dispatch(action: NavAction) {
        currentState = reducer.reduce(state, action)
        if (isReadyToProcess) {
            render.render(currentState)
        } else {
            isStatePending = true
        }
    }

    private fun onDialogDismissed() {
        currentState = currentState.copy(dialogState = DialogNavState(null))
    }
}

private const val BUNDLE_KEY_STATE = "alc_state"
