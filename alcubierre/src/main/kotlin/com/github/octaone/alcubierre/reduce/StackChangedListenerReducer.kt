package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.state.AnyRootNavState

class StackChangedListenerReducer(
    private val delegate: NavReducer<AnyRootNavState>,
    private val onStackChanged: (from: Int, to: Int) -> Unit
): NavReducer<AnyRootNavState> {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState {
        val newState = delegate.reduce(state, action)
        if (state.currentStackId != newState.currentStackId) {
            onStackChanged(state.currentStackId, newState.currentStackId)
        }
        return newState
    }
}

fun NavReducer<AnyRootNavState>.addOnStackChangedListener(
    onStackChanged: (from: Int, to: Int) -> Unit
): NavReducer<AnyRootNavState> =
    StackChangedListenerReducer(
        delegate = this,
        onStackChanged = onStackChanged
    )
