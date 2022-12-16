package com.github.octaone.alcubierre.screen

import android.os.Parcelable

/**
 * Base screen (specific destination for navigation)
 */
interface Screen : Parcelable, Extras {

    val screenId: ScreenId
}
