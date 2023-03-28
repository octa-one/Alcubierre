package com.github.octaone.alcubierre.screen

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer

/**
 * Base screen (specific destination for navigation)
 */
interface Screen : Parcelable, ExtrasContainer {

    val screenId: ScreenId
}
