package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.internal.ScreenParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

/**
 * State of single stack
 */
@Parcelize
data class StackNavState<out S : Screen>(
    val chain: List<@WriteWith<ScreenParceler> S>
): Parcelable {

    companion object {
        val EMPTY = StackNavState<Nothing>(emptyList())
    }
}
