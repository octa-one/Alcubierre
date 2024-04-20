@file:OptIn(AlcubierreInternalApi::class)

package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.base.util.getNotNull
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import kotlinx.parcelize.Parcelize

/**
 * State of entire navigation of application (dialogs and every stack).
 */
@Parcelize
data class RootNavState<out S : Screen, out D : Dialog>(
    val dialogState: DialogNavState<D>,
    val stackStates: Map<Int, StackNavState<S>>,
    val currentStackId: Int
): Parcelable {

    val currentStackState: StackNavState<S> get() = stackStates.getNotNull(currentStackId)

    val currentScreen: S? get() = currentStackState.stack.lastOrNull()

    val currentDialog: D? get() = dialogState.queue.firstOrNull()

    companion object {

        val EMPTY = RootNavState(
            dialogState = DialogNavState.EMPTY,
            stackStates = hashMapOf(-1 to StackNavState.EMPTY),
            currentStackId = -1
        )
    }
}
