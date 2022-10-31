package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.Back
import com.github.octaone.alcubierre.action.BackTo
import com.github.octaone.alcubierre.action.BackToRoot
import com.github.octaone.alcubierre.action.Forward
import com.github.octaone.alcubierre.action.NavigationAction
import com.github.octaone.alcubierre.action.Replace
import com.github.octaone.alcubierre.action.ReplaceRoot
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.StackNavigationState

/**
 * [NavigationReducer], отвечающий за команды над конкретным стеком.
 */
class AlcubierreStackNavigationReducer : NavigationReducer<StackNavigationState> {

    override fun reduce(
        state: StackNavigationState,
        action: NavigationAction
    ): StackNavigationState =
        when (action) {
            is Forward -> {
                state.modifyChain { this + action.screens }
            }
            is Replace -> {
                state.modifyChain { dropLast(1) + action.screens }
            }
            is Back -> {
                if (state.chain.size > 1) {
                    state.modifyChain { dropLast(1) }
                } else {
                    state
                }
            }
            is BackTo -> {
                val i = state.chain.indexOfLast { it.id == action.screenId }
                if (i != -1) state.modifyChain { take(i + 1) }
                else state
            }
            is BackToRoot -> {
                state.modifyChain { listOfNotNull(firstOrNull()) }
            }
            is ReplaceRoot -> {
                state.copy(chain = action.screens)
            }
            else -> {
                state
            }
    }

    private inline fun StackNavigationState.modifyChain(update: MutableList<Screen>.() -> List<Screen>): StackNavigationState =
        copy(chain = chain.toMutableList().update())
}
