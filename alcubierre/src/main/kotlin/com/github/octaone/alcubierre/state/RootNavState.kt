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
