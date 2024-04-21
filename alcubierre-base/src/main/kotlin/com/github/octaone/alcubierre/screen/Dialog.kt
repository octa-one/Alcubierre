package com.github.octaone.alcubierre.screen

import android.os.Parcelable
import androidx.annotation.IntRange
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import java.util.UUID

/**
 * Base dialog
 */
public abstract class Dialog : Parcelable, Comparable<Dialog>, ExtrasContainer {

    @get:IntRange(0, Long.MAX_VALUE)
    public abstract val priority: Int

    public var isShowing: Boolean = false
        @AlcubierreInternalApi set

    public var dialogId: String = UUID.randomUUID().toString()
        @AlcubierreInternalApi set

    override fun compareTo(other: Dialog): Int = priority.compareTo(other.priority)
}
