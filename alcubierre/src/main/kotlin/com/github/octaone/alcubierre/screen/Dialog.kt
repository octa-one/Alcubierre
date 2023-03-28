package com.github.octaone.alcubierre.screen

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer

/**
 * Base dialog
 */
interface Dialog : Parcelable, Comparable<Dialog>, ExtrasContainer {

    val dialogId: ScreenId

    val priority: Int

    override fun compareTo(other: Dialog): Int = priority.compareTo(other.priority)
}
