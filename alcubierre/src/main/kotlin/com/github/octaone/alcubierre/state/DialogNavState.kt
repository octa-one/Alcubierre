package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.internal.DialogParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

/**
 * State of dialogs
 */
@Parcelize
data class DialogNavState<out D : Dialog>(
    val queue: List<@WriteWith<DialogParceler> D>
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DialogNavState<*>

        if (queue.size != other.queue.size) return false
        for (i in queue.indices) {
            if (queue[i].dialogId != other.queue[i].dialogId) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = 1
        for (i in queue.indices) result = 31 * result + queue[i].dialogId.hashCode()
        return result
    }

    companion object {
        val EMPTY = DialogNavState<Nothing>(emptyList())
    }
}
