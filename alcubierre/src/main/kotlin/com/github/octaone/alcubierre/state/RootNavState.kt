package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.util.getNotNull
import kotlinx.parcelize.Parcelize

/**
 * State of entire navigation of application (dialog and every stack)
 */
@Parcelize
data class RootNavState(
    val dialogState: DialogNavState,
    val stackStates: Map<Int, StackNavState>,
    val currentStackId: Int
): Parcelable {

    val currentStackState: StackNavState get() = stackStates.getNotNull(currentStackId)
    val currentDialog: Dialog? get() = dialogState.queue.firstOrNull()

    companion object {

        val EMPTY = RootNavState(
            dialogState = DialogNavState.EMPTY,
            stackStates = hashMapOf(-1 to StackNavState.EMPTY),
            currentStackId = -1
        )
    }
}
