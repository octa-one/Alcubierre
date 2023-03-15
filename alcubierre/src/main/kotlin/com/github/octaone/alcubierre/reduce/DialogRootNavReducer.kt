package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.Back
import com.github.octaone.alcubierre.action.DismissDialog
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.action.ShowDialog
import com.github.octaone.alcubierre.state.DialogNavState
import com.github.octaone.alcubierre.state.RootNavState

/**
 * Reducer responds for action with dialogs
 */
class DialogRootNavReducer(
    private val origin: NavReducer<RootNavState>,
    private val dialogReducer: NavReducer<DialogNavState> = DialogNavReducer()
) : NavReducer<RootNavState> {

    override fun reduce(state: RootNavState, action: NavAction) = when (action) {
        is ShowDialog, DismissDialog -> {
            state.copy(dialogState = dialogReducer.reduce(state.dialogState, action))
        }
        is Back -> { // Back сначала закрывает диалог, если он отображается.
            if (state.dialogState.queue.isEmpty()) {
                origin.reduce(state, action)
            } else {
                state.copy(dialogState = dialogReducer.reduce(state.dialogState, DismissDialog))
            }
        }
        else -> {
            // Остальные действия обрабатываются дальнейшей цепочкой редьюсеров, но диалог закрывается.
            if (state.dialogState.queue.isEmpty()) {
                origin.reduce(state, action)
            } else {
                origin.reduce(state.copy(dialogState = DialogNavState.EMPTY), action)
            }
        }
    }
}
