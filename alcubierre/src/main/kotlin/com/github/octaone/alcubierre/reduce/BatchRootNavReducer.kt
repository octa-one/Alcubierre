package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.action.Batch
import com.github.octaone.alcubierre.state.AnyRootNavState

class BatchRootNavReducer : LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState =
        when (action) {
            is Batch -> {
                action.actions.fold(state) { foldState, foldAction -> head.reduce(foldState, foldAction) }
            }
            else -> {
                next.reduce(state, action)
            }
        }
}
