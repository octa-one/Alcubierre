package com.github.octaone.alcubierre.screen

import android.os.Parcelable

/**
 * Base dialog
 */
interface Dialog : Parcelable, Extras {

    val dialogId: ScreenId
}
