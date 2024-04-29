package com.github.octaone.alcubierre.screen

import android.net.Uri
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer

/**
 * An extension over any [ExtrasContainer] (eg [Screen], [Dialog]),
 * to check if it has been opened using a deeplink.
 */
public val ExtrasContainer.isFromDeeplink: Boolean
    get() = hasExtras() && extras.containsKey(DEEPLINK_URI)

/**
 * An extension over any [ExtrasContainer] (eg [Screen], [Dialog]),
 * to get the deeplink [Uri] it has been opened with.
 */
public val ExtrasContainer.deeplinkUri: Uri?
    get() = if (hasExtras()) {
        extras.getParcelable(DEEPLINK_URI, Uri::class.java)
    } else {
        null
    }

internal const val DEEPLINK_URI = "com.github.octaone.alcubierre.DEEPLINK_URI"
