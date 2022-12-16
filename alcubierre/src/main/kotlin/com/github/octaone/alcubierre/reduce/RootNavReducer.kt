package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.ApplyState
import com.github.octaone.alcubierre.action.Back
import com.github.octaone.alcubierre.action.BackTo
import com.github.octaone.alcubierre.action.BackToRoot
import com.github.octaone.alcubierre.action.ClearStack
import com.github.octaone.alcubierre.action.Forward
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.action.NewStack
import com.github.octaone.alcubierre.action.Replace
import com.github.octaone.alcubierre.action.ReplaceRoot
import com.github.octaone.alcubierre.action.SelectStack
import com.github.octaone.alcubierre.state.RootNavState
import com.github.octaone.alcubierre.state.StackNavState
import com.github.octaone.alcubierre.util.getNotNull

/**
 * [NavReducer] responds for commands with stacks and retranslate remaining command to proper [stackReducer]
 */
class AlcubierreRootNavReducer(
    private val stackReducer: NavReducer<StackNavState> = AlcubierreStackNavReducer()
) : NavReducer<RootNavState> {

    override fun reduce(state: RootNavState, action: NavAction) = when (action) {
        is Forward, is Back, is Replace, is BackToRoot, is ReplaceRoot, is BackTo -> {
            state.modifyStack(state.currentStackId) { stackReducer.reduce(this, action) }
        }
        is NewStack -> {
            state.setStack(action.stackId, StackNavState(action.screens))
        }
        is SelectStack -> {
            if (state.currentStackId == action.stackId) {
                state
            } else {
                check(state.stackStates.containsKey(action.stackId))
                state.copy(currentStackId = action.stackId)
            }
        }
        is ClearStack -> {
            if (state.stackStates.containsKey(action.stackId)) {
                check(state.currentStackId != action.stackId)
                state.copy(stackStates = state.stackStates.toMutableMap().apply { remove(action.stackId) })
            } else {
                state
            }
        }
        is ApplyState -> {
            action.state
        }
        else -> {
            state
        }
    }

    private inline fun RootNavState.modifyStack(id: Int, update: StackNavState.() -> StackNavState): RootNavState =
        setStack(id, stackStates.getNotNull(id).update())

    private fun RootNavState.setStack(id: Int, stack: StackNavState): RootNavState =
        copy(stackStates = stackStates.toMutableMap().apply { put(id, stack) })
}
