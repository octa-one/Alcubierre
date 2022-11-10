package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.state.RootNavState

class StackChangedListenerReducer(
    private val delegate: NavReducer<RootNavState>,
    private val onStackChanged: (from: Int, to: Int) -> Unit
): NavReducer<RootNavState> {

    override fun reduce(state: RootNavState, action: NavAction): RootNavState {
        val newState = delegate.reduce(state, action)
        if (state.currentStackId != newState.currentStackId) {
            onStackChanged(state.currentStackId, newState.currentStackId)
        }
        return newState
    }
}

fun NavReducer<RootNavState>.addOnStackChangedListener(
    onStackChanged: (from: Int, to: Int) -> Unit
): NavReducer<RootNavState> =
    StackChangedListenerReducer(
        delegate = this,
        onStackChanged = onStackChanged
    )
