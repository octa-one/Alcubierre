package com.github.octaone.alcubierre.render.internal

import androidx.compose.runtime.Immutable
import com.github.octaone.alcubierre.state.ComposeRootNavState

/**
 * Compose runtime checks if a function can be skipped by comparing its arguments.
 * This class will ignore any changes made to the screen stacks if the dialog remains unchanged.
 */
@Immutable
internal class DialogRootNavStateProjection(
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
