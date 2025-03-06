package com.github.octaone.alcubierre.render.internal

import androidx.compose.runtime.Immutable
import com.github.octaone.alcubierre.state.ComposeRootNavState

/**
 * Compose runtime checks if a function can be skipped by comparing its arguments.
 * This class will ignore any changes made to the dialog queue if the current screen remains unchanged.
 */
@Immutable
internal class ScreenRootNavStateProjection(
    val state: ComposeRootNavState
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScreenRootNavStateProjection

        return state.currentScreen?.screenId == other.state.currentScreen?.screenId
    }

    // The hashCode/equals contract is not respected, but in this case it doesn't need to be.
    override fun hashCode(): Int = 0
}
