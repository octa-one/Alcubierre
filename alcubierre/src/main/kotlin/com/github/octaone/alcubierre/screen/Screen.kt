package com.github.octaone.alcubierre.screen

import android.os.Parcelable
import com.github.octaone.alcubierre.screen.extra.ParcelableExtras

/**
 * Base screen (specific destination for navigation)
 */
interface Screen : Parcelable, ParcelableExtras {

    val screenId: ScreenId
}
