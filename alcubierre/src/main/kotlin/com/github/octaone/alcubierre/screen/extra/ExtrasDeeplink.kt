package com.github.octaone.alcubierre.screen.extra

import android.net.Uri
import com.github.octaone.alcubierre.ExtrasKeys

val ParcelableExtras.isFromDeeplink: Boolean
    get() = containsKey(ExtrasKeys.DEEPLINK_URI)

val ParcelableExtras.deeplinkUri: Uri?
    get() = if (containsKey(ExtrasKeys.DEEPLINK_URI)) {
        getParcelable(ExtrasKeys.DEEPLINK_URI, Uri::class.java)
    } else {
        null
    }
