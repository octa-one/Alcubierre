package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.util.getNotNull
import kotlinx.parcelize.Parcelize

/**
 * Состояние всей навигации в приложении (диалога и каждого стека).
 */
@Parcelize
data class RootNavState(
    val dialog: Dialog?,
    val stacks: Map<Int, StackNavState>,
    val currentStackId: Int
): Parcelable {

    val currentStackState: StackNavState get() = stacks.getNotNull(currentStackId)

    companion object {

        val EMPTY = RootNavState(
            dialog = null,
            stacks = mapOf(-1 to StackNavState.EMPTY),
            currentStackId = -1
        )
    }
}
