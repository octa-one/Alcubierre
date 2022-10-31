package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.ApplyState
import com.github.octaone.alcubierre.action.Back
import com.github.octaone.alcubierre.action.BackTo
import com.github.octaone.alcubierre.action.BackToRoot
import com.github.octaone.alcubierre.action.Forward
import com.github.octaone.alcubierre.action.NavigationAction
import com.github.octaone.alcubierre.action.NewStack
import com.github.octaone.alcubierre.action.Replace
import com.github.octaone.alcubierre.action.ReplaceRoot
import com.github.octaone.alcubierre.action.SelectStack
import com.github.octaone.alcubierre.state.RootNavigationState
import com.github.octaone.alcubierre.state.StackNavigationState
import com.github.octaone.alcubierre.util.getNotNull

/**
 * [NavigationReducer], отвечающий за команды со стеками и перенаправляющий остальные команды в нужный [stackReducer].
 */
class AlcubierreRootNavigationReducer(
    private val stackReducer: NavigationReducer<StackNavigationState> = AlcubierreStackNavigationReducer()
) : NavigationReducer<RootNavigationState> {

    override fun reduce(state: RootNavigationState, action: NavigationAction) = when (action) {
        is Forward, is Back, is Replace, is BackToRoot, is ReplaceRoot, is BackTo -> {
            state.modifyStack(state.currentStackId) { stackReducer.reduce(this, action) }
        }
        is NewStack -> {
            state.setStack(action.stackId, StackNavigationState(action.screens))
        }
        is SelectStack -> {
            check(state.stacks.containsKey(action.stackId))
            state.copy(currentStackId = action.stackId)
        }
        is ApplyState -> {
            action.state
        }
        else -> {
            state
        }
    }

    private inline fun RootNavigationState.modifyStack(id: Int, update: StackNavigationState.() -> StackNavigationState): RootNavigationState =
        setStack(id, stacks.getNotNull(id).update())

    private fun RootNavigationState.setStack(id: Int, stack: StackNavigationState): RootNavigationState =
        copy(stacks = stacks.toMutableMap().apply { put(id, stack) })
}
