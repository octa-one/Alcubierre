package com.github.octaone.alcubierre.render.internal

import androidx.compose.runtime.Immutable
import com.github.octaone.alcubierre.state.ComposeRootNavState

@Immutable
class DialogRootNavStateProjection(
    val state: ComposeRootNavState
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DialogRootNavStateProjection

        return state.currentDialog?.dialogId == other.state.currentDialog?.dialogId
    }

    // The hashCode/equals contract is not honored, but in this case it doesn't need to be.
    override fun hashCode(): Int = state.hashCode()
}
