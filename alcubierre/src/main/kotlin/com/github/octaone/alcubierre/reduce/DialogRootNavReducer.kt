package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.action.Back
import com.github.octaone.alcubierre.action.DismissDialog
import com.github.octaone.alcubierre.action.ShowDialog
import com.github.octaone.alcubierre.state.AnyDialogNavState
import com.github.octaone.alcubierre.state.AnyRootNavState
import com.github.octaone.alcubierre.state.DialogNavState
import com.github.octaone.alcubierre.state.RootNavState

/**
 * [NavReducer] for dialog specific actions. Responsible for [RootNavState].
 * Forwards actions to [dialogReducer] and updates [RootNavState]
 * with the updated [DialogNavState] from [dialogReducer].
 *
 * [Back] action closes the current dialog, the same as [DismissDialog].
 *
 * @param dialogReducer [NavReducer] that can reduce [DialogNavState].
 * @param closeDialogsOnActions Whether dialogs should be closed on navigation actions.
 * If false, it will be possible to update screens under dialogs.
 */
public class DialogRootNavReducer(
    private val dialogReducer: NavReducer<AnyDialogNavState> = DialogNavReducer(),
    private val closeDialogsOnActions: Boolean = true
) : LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState = when (action) {
        is ShowDialog, DismissDialog -> {
            state.copy(dialogState = dialogReducer.reduce(state.dialogState, action))
        }
        is Back -> { // Back first closes the dialog if it is displayed.
            if (state.dialogState.queue.isEmpty()) {
                next.reduce(state, action)
            } else {
                state.copy(dialogState = dialogReducer.reduce(state.dialogState, DismissDialog))
            }
        }
        else -> {
            if (!closeDialogsOnActions || state.dialogState.queue.isEmpty()) {
                next.reduce(state, action)
            } else {
                // The remaining actions are processed by reducers chain, but the dialog will be closed.
                next.reduce(state.copy(dialogState = DialogNavState.EMPTY), action)
            }
        }
    }
}
