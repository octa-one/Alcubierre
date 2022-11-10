package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Screen
import kotlinx.parcelize.Parcelize

/**
 * Состояние одного стека.
 */
@Parcelize
data class StackNavState(
    val chain: List<Screen>
): Parcelable {

    companion object {
        val EMPTY = StackNavState(emptyList())
    }
}
