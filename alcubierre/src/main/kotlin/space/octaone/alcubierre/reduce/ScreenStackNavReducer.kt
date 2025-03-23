package space.octaone.alcubierre.reduce

import space.octaone.alcubierre.action.Back
import space.octaone.alcubierre.action.BackTo
import space.octaone.alcubierre.action.BackToRoot
import space.octaone.alcubierre.action.Forward
import space.octaone.alcubierre.action.Replace
import space.octaone.alcubierre.action.ReplaceRoot
import space.octaone.alcubierre.base.action.AnyNavAction
import space.octaone.alcubierre.base.reduce.NavReducer
import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.base.state.AnyStackNavState
import space.octaone.alcubierre.util.optimizeReadOnlyList

/**
 * [space.octaone.alcubierre.base.reduce.NavReducer] responds for commands with specific stack
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
                check(action.screens.isNotEmpty())
                state.copy(stack = action.screens)
            }
            else -> {
                state
            }
    }

    private inline fun AnyStackNavState.modifyStack(update: MutableList<Screen>.() -> Unit): AnyStackNavState =
        copy(stack = stack.toMutableList().apply(update).optimizeReadOnlyList())
}
