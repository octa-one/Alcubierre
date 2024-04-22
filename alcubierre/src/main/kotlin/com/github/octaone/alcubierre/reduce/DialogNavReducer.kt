package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.action.DismissDialog
import com.github.octaone.alcubierre.action.ShowDialog
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.state.AnyDialogNavState
import com.github.octaone.alcubierre.state.DialogNavState
import com.github.octaone.alcubierre.util.optimizeReadOnlyList

public class DialogNavReducer : NavReducer<AnyDialogNavState> {

    override fun reduce(state: AnyDialogNavState, action: AnyNavAction): AnyDialogNavState = when (action) {
        is ShowDialog -> {
            if (state.queue.isEmpty()) {
                state.copy(queue = listOf(action.dialog))
            } else {
                var insertIndex = state.queue.indexOfFirst { it.priority < action.dialog.priority && !it.isShowing}
                if (insertIndex == -1) insertIndex = state.queue.size
                state.modifyQueue { add(insertIndex, action.dialog) }
            }
        }
        is DismissDialog -> {
            if (state.queue.size <= 1) {
                DialogNavState.EMPTY
            } else {
                state.modifyQueue { removeAt(0) }
            }
        }
        else -> state
    }

    private inline fun AnyDialogNavState.modifyQueue(update: MutableList<Dialog>.() -> Unit): AnyDialogNavState =
        copy(queue = queue.toMutableList().apply(update).optimizeReadOnlyList())
}
