package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.Batch
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.state.RootNavState

class BatchRootNavReducer(
    private val origin: NavReducer<RootNavState>
) : NavReducer<RootNavState> {

    override fun reduce(state: RootNavState, action: NavAction): RootNavState =
        when (action) {
            is Batch -> {
                action.actions.fold(state) { foldState, foldAction -> reduce(foldState, foldAction) }
            }
            else -> {
                origin.reduce(state, action)
            }
        }
}
