package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.action.Back
import com.github.octaone.alcubierre.action.BackTo
import com.github.octaone.alcubierre.action.BackToRoot
import com.github.octaone.alcubierre.action.Forward
import com.github.octaone.alcubierre.action.Replace
import com.github.octaone.alcubierre.action.ReplaceRoot
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.AnyStackNavState

/**
 * [NavReducer] responds for commands with specific stack
 */
class AlcubierreStackNavReducer : NavReducer<AnyStackNavState> {

    override fun reduce(
        state: AnyStackNavState,
        action: AnyNavAction
    ): AnyStackNavState =
        when (action) {
            is Forward -> {
                state.modifyStack { this + action.screens }
            }
            is Replace -> {
                state.modifyStack { dropLast(1) + action.screens }
            }
            is Back -> {
                if (state.stack.size > 1) {
                    state.modifyStack { dropLast(1) }
                } else {
                    state
                }
            }
            is BackTo -> {
                val i = state.stack.indexOfLast { it.screenId == action.screen.screenId }
                if (i != -1) state.modifyStack { take(if (action.inclusive) i else i + 1) }
                else state
            }
            is BackToRoot -> {
                state.modifyStack { listOfNotNull(firstOrNull()) }
            }
            is ReplaceRoot -> {
                state.copy(stack = action.screens)
            }
            else -> {
                state
            }
    }

    private inline fun AnyStackNavState.modifyStack(update: MutableList<Screen>.() -> List<Screen>): AnyStackNavState =
        copy(stack = stack.toMutableList().update())
}
