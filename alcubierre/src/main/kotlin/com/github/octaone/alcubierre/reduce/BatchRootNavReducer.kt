package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.action.Batch
import com.github.octaone.alcubierre.state.AnyRootNavState

class BatchRootNavReducer(
    private val origin: NavReducer<AnyRootNavState>
) : NavReducer<AnyRootNavState> {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState =
        when (action) {
            is Batch -> {
                action.actions.fold(state) { foldState, foldAction -> reduce(foldState, foldAction) }
            }
            else -> {
                origin.reduce(state, action)
            }
        }
}
