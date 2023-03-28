package com.github.octaone.alcubierre.screen

import android.net.Uri
import com.github.octaone.alcubierre.ExtrasKeys
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer

val ExtrasContainer.isFromDeeplink: Boolean
    get() = hasExtras() && extras.containsKey(ExtrasKeys.DEEPLINK_URI)

val ExtrasContainer.deeplinkUri: Uri?
    get() = if (hasExtras()) {
        extras.getParcelable(ExtrasKeys.DEEPLINK_URI, Uri::class.java)
    } else {
        null
    }
