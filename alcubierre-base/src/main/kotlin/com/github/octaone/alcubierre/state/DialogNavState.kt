package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.internal.DialogParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

/**
 * State of a dialogs queue.
 *
 * @param queue Dialogs queue, if multiple dialog actions were called at the same time.
 * The first dialog in the queue is the one the user sees.
 * Default reducer fills the queue based on the priority field.
 *
 * [equals] only uses [Dialog.dialogId] to compare dialogs.
 */
@Parcelize
public data class DialogNavState<out D : Dialog>(
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

    public companion object {
        public val EMPTY: DialogNavState<Nothing> = DialogNavState(emptyList())
    }
}
