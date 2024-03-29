package com.github.octaone.alcubierre.screen

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import java.util.UUID

/**
 * Base screen (specific destination for navigation)
 */
abstract class Screen : Parcelable, ExtrasContainer {

    var screenId: String = UUID.randomUUID().toString()
        internal set
}
