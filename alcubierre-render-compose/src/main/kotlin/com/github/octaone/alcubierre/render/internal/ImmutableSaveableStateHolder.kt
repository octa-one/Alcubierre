package com.github.octaone.alcubierre.render.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.saveable.SaveableStateHolder

/**
 * Immutable SaveableStateHolder wrapper.
 */
@Immutable
internal class ImmutableSaveableStateHolder(
    private val holder: SaveableStateHolder
) : SaveableStateHolder {

    @Composable
    @NonRestartableComposable
    override fun SaveableStateProvider(key: Any, content: @Composable () -> Unit) =
        holder.SaveableStateProvider(key, content)

    override fun removeState(key: Any) =
        holder.removeState(key)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImmutableSaveableStateHolder

        return holder == other.holder
    }

    override fun hashCode(): Int =
        holder.hashCode()
}
