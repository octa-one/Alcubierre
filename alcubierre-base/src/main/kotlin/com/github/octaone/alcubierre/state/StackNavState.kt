package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.internal.ScreenParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

/**
 * State of a single stack.
 *
 * @param stack Screens stack. The last screen in the stack is the one the user sees.
 * Default reducer fills the queue based on the priority field.
 *
 * [equals] only uses [Screen.screenId] to compare screens.
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
        public val EMPTY: StackNavState<Nothing> = StackNavState(emptyList())
    }
}
