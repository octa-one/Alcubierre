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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StackNavState<*>

        if (chain.size != other.chain.size) return false
        for (i in chain.indices) {
            if (chain[i].screenId != other.chain[i].screenId) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = 1
        for (i in chain.indices) result = 31 * result + chain[i].screenId.hashCode()
        return result
    }

    companion object {
        val EMPTY = StackNavState<Nothing>(emptyList())
    }
}
