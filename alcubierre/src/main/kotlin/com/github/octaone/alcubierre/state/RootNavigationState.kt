package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.util.getNotNull
import kotlinx.parcelize.Parcelize

/**
 * Состояние всей навигации в приложении (диалога и каждого стека).
 */
@Parcelize
data class RootNavigationState(
    val dialog: Dialog?,
    val stacks: Map<Int, StackNavigationState>,
    val currentStackId: Int
): Parcelable {

    val currentStackState: StackNavigationState get() = stacks.getNotNull(currentStackId)

    companion object {

        val EMPTY = RootNavigationState(
            dialog = null,
            stacks = mapOf(-1 to StackNavigationState.EMPTY),
            currentStackId = -1
        )
    }
}
