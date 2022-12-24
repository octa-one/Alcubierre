package com.github.octaone.alcubierre.state

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.internal.DialogParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

/**
 * State of dialogs
 */
@Parcelize
@TypeParceler<Dialog, DialogParceler>
data class DialogNavState(
    val dialog: Dialog?
) : Parcelable
