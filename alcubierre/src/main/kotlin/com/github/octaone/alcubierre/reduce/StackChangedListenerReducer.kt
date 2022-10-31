package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.NavigationAction
import com.github.octaone.alcubierre.state.RootNavigationState

class StackChangedListenerReducer(
    private val delegate: NavigationReducer<RootNavigationState>,
    private val onStackChanged: (from: Int, to: Int) -> Unit
): NavigationReducer<RootNavigationState> {

    override fun reduce(state: RootNavigationState, action: NavigationAction): RootNavigationState {
        val newState = delegate.reduce(state, action)
        if (state.currentStackId != newState.currentStackId) {
            onStackChanged(state.currentStackId, newState.currentStackId)
        }
        return newState
    }
}

fun NavigationReducer<RootNavigationState>.addOnStackChangedListener(
    onStackChanged: (from: Int, to: Int) -> Unit
): NavigationReducer<RootNavigationState> =
    StackChangedListenerReducer(
        delegate = this,
        onStackChanged = onStackChanged
    )
