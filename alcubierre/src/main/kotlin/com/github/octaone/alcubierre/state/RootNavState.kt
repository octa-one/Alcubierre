package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.util.getNotNull
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * State of entire navigation of application (dialog and every stack)
 */
@Parcelize
data class RootNavState<out S : Screen, out D : Dialog>(
    val dialogState: DialogNavState<D>,
    val stackStates: Map<Int, StackNavState<S>>,
    val currentStackId: Int
): Parcelable {

    @IgnoredOnParcel
    val currentStackState: StackNavState<S> = stackStates.getNotNull(currentStackId)

    @IgnoredOnParcel
    val currentScreen: S? = currentStackState.chain.lastOrNull()

    @IgnoredOnParcel
    val currentDialog: D? = dialogState.queue.firstOrNull()

    companion object {

        val EMPTY = RootNavState(
            dialogState = DialogNavState.EMPTY,
            stackStates = hashMapOf(-1 to StackNavState.EMPTY),
            currentStackId = -1
        )
    }
}
