package com.github.octaone.alcubierre.screen

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import java.util.UUID

/**
 * Base dialog
 */
abstract class Dialog : Parcelable, Comparable<Dialog>, ExtrasContainer {

    abstract val priority: Int

    var dialogId: String = UUID.randomUUID().toString()
        internal set

    override fun compareTo(other: Dialog): Int = priority.compareTo(other.priority)
}
