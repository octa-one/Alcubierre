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
public data class StackNavState<out S : Screen>(
    val stack: List<@WriteWith<ScreenParceler> S>
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StackNavState<*>

        if (stack.size != other.stack.size) return false
        for (i in stack.indices) {
            if (stack[i].screenId != other.stack[i].screenId) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = 1
        for (i in stack.indices) result = 31 * result + stack[i].screenId.hashCode()
        return result
    }

    public companion object {
        public val EMPTY: StackNavState<Nothing> = StackNavState<Nothing>(emptyList())
    }
}
