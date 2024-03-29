package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.action.Back
import com.github.octaone.alcubierre.action.DismissDialog
import com.github.octaone.alcubierre.action.ShowDialog
import com.github.octaone.alcubierre.state.AnyDialogNavState
import com.github.octaone.alcubierre.state.AnyRootNavState
import com.github.octaone.alcubierre.state.DialogNavState

/**
 * Reducer responds for action with dialogs
 */
class DialogRootNavReducer(
    private val origin: NavReducer<AnyRootNavState>,
    private val dialogReducer: NavReducer<AnyDialogNavState> = DialogNavReducer()
) : NavReducer<AnyRootNavState> {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction) = when (action) {
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
