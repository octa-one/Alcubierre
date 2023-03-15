package com.github.octaone.alcubierre.screen

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.extra.ParcelableExtras

/**
 * Base dialog
 */
interface Dialog : Parcelable, Comparable<Dialog>, ParcelableExtras {

    val dialogId: ScreenId

    val priority: Int

    override fun compareTo(other: Dialog): Int = priority.compareTo(other.priority)
}
