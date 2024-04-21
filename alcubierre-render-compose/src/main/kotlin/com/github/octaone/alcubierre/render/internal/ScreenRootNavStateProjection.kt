package com.github.octaone.alcubierre.render.internal

import androidx.compose.runtime.Immutable
import com.github.octaone.alcubierre.state.ComposeRootNavState

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

    // The hashCode/equals contract is not honored, but in this case it doesn't need to be.
    override fun hashCode(): Int = state.hashCode()
}
