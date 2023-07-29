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

    companion object {
        val EMPTY = DialogNavState<Nothing>(emptyList())
    }
}
