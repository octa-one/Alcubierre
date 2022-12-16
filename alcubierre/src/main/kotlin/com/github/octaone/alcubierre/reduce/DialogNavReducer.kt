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
class DialogNavReducer(
    private val origin: NavReducer<RootNavState>
) : NavReducer<RootNavState> {

    override fun reduce(state: RootNavState, action: NavAction) = when (action) {
        is ShowDialog -> {
            state.copy(dialogState = DialogNavState(action.dialog))
        }
        is DismissDialog -> {
            state.copy(dialogState = DialogNavState(null))
        }
        is Back -> { // Back сначала закрывает диалог, если он отображается.
            if (state.currentDialog == null) {
                origin.reduce(state, action)
            } else {
                state.copy(dialogState = DialogNavState(null))
            }
        }
        else -> {
            // Остальные действия обрабатываются дальнейшей цепочкой редьюсеров, но диалог закрывается.
            if (state.currentDialog == null) {
                origin.reduce(state, action)
            } else {
                origin.reduce(state.copy(dialogState = DialogNavState(null)), action)
            }
        }
    }
}
