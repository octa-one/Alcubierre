package com.github.octaone.alcubierre.screen

import android.net.Uri
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer

val ExtrasContainer.isFromDeeplink: Boolean
    get() = hasExtras() && extras.containsKey(DEEPLINK_URI)

val ExtrasContainer.deeplinkUri: Uri?
    get() = if (hasExtras()) {
        extras.getParcelable(DEEPLINK_URI, Uri::class.java)
    } else {
        null
    }

internal const val DEEPLINK_URI = "com.github.octaone.alcubierre.DEEPLINK_URI"
