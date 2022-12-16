package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.internal.ScreenParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

/**
 * State of single stack
 */
@Parcelize
@TypeParceler<Screen, ScreenParceler>
data class StackNavState(
    val chain: List<Screen>
): Parcelable {

    companion object {
        val EMPTY = StackNavState(emptyList())
    }
}
