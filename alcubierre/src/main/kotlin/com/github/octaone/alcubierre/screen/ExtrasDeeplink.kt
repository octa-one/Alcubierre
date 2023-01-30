package com.github.octaone.alcubierre.screen

import android.net.Uri
import com.github.octaone.alcubierre.ExtrasKeys
import com.github.octaone.alcubierre.util.getParcelableCompat

val Extras.isFromDeeplink: Boolean
    get() = if (hasExtras()) extras.containsKey(ExtrasKeys.DEEPLINK_URI) else false

val Extras.deeplinkUri: Uri?
    get() = if (hasExtras()) extras.getParcelableCompat(ExtrasKeys.DEEPLINK_URI) else null
