package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.DismissDialog
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.action.ShowDialog
import com.github.octaone.alcubierre.screen.isShowing
import com.github.octaone.alcubierre.state.DialogNavState

class DialogNavReducer : NavReducer<DialogNavState> {

    override fun reduce(state: DialogNavState, action: NavAction): DialogNavState = when (action) {
        is ShowDialog -> {
            if (state.queue.isEmpty()) {
                state.copy(queue = listOf(action.dialog))
            } else {
                var insertIndex = state.queue.indexOfFirst { it.priority < action.dialog.priority && !it.isShowing}
                if (insertIndex == -1) insertIndex = state.queue.size
                state.copy(queue = state.queue.toMutableList().apply { add(insertIndex, action.dialog) })
            }
        }
        is DismissDialog -> {
            if (state.queue.size <= 1) {
                DialogNavState.EMPTY
            } else {
                state.copy(queue = state.queue.drop(1))
            }
        }
        else -> state
    }
}

