package com.github.octaone.alcubierre.screen

import android.os.Parcelable
import androidx.annotation.IntRange
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import java.util.UUID

/**
 * Abstraction for describing dialog destination for navigation.
 *
 * The main difference from [Screen] is that the [Dialog] is shown outside the stack on top of any content.
 * And also there can only be one shown dialog at a time.
 * The remaining added dialogs will be queued according to [priority].
 *
 * Priority handling depends on the implementation of the reducer.
 * The default implementation assumes that the visible dialog has the highest priority.
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
