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
 * @param [dialogState] Dialogs, see [DialogNavState].
 * @param [stackStates] Id of each stack with the corresponding [StackNavState].
 * @param [currentStackId] Id of the currently selected stack.
 */
@Parcelize
public data class RootNavState<out S : Screen, out D : Dialog>(
    val dialogState: DialogNavState<D>,
    val stackStates: Map<Int, StackNavState<S>>,
    val currentStackId: Int
): Parcelable {

    /**
     * [StackNavState] for [currentStackId] stack.
     */
    val currentStackState: StackNavState<S> get() = stackStates.getNotNull(currentStackId)

    /**
     * Currently visible Screen (last Screen in [currentStackId] stack).
     */
    val currentScreen: S? get() = currentStackState.stack.lastOrNull()

    /**
     * Currently visible Dialog (first Dialog in the queue).
     */
    val currentDialog: D? get() = dialogState.queue.firstOrNull()

    public companion object {

        public val EMPTY: RootNavState<Nothing, Nothing> = RootNavState(
            dialogState = DialogNavState.EMPTY,
            stackStates = mapOf(-1 to StackNavState.EMPTY),
            currentStackId = -1
        )
    }
}
