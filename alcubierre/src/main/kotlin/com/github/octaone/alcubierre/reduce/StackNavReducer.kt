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
import com.github.octaone.alcubierre.util.optimizeReadOnlyList

/**
 * [NavReducer] responds for commands with specific stack
 */
public class ScreenStackNavReducer : NavReducer<AnyStackNavState> {

    override fun reduce(
        state: AnyStackNavState,
        action: AnyNavAction
    ): AnyStackNavState =
        when (action) {
            is Forward -> {
                state.modifyStack { addAll(action.screens) }
            }
            is Replace -> {
                state.modifyStack {
                    removeAt(lastIndex)
                    addAll(action.screens)
                }
            }
            is Back -> {
                if (state.stack.size > 1) {
                    state.modifyStack { removeAt(lastIndex) }
                } else {
                    state
                }
            }
            is BackTo -> {
                val i = state.stack.indexOfLast { it.screenId == action.screen.screenId }
                if (i != -1) {
                    state.modifyStack {
                        repeat(lastIndex - i) { removeAt(lastIndex) }
                        if (action.inclusive) removeAt(lastIndex)
                    }
                } else {
                    state
                }
            }
            is BackToRoot -> {
                state.modifyStack {
                    val root = getOrNull(0)
                    clear()
                    if (root != null) add(root)
                }
            }
            is ReplaceRoot -> {
                state.copy(stack = action.screens)
            }
            else -> {
                state
            }
    }

    private inline fun AnyStackNavState.modifyStack(update: MutableList<Screen>.() -> Unit): AnyStackNavState =
        copy(stack = stack.toMutableList().apply(update).optimizeReadOnlyList())
}
