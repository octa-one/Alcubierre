package space.octaone.alcubierre.screen

import android.net.Uri
import space.octaone.alcubierre.base.screen.extra.ExtrasContainer

/**
 * An extension over any [ExtrasContainer] (eg [space.octaone.alcubierre.base.screen.Screen], [space.octaone.alcubierre.base.screen.Dialog]),
 * to check if it has been opened using a deeplink.
 */
public val ExtrasContainer.isFromDeeplink: Boolean
    get() = hasExtras() && extras.containsKey(DEEPLINK_URI)

/**
 * An extension over any [ExtrasContainer] (eg [space.octaone.alcubierre.base.screen.Screen], [space.octaone.alcubierre.base.screen.Dialog]),
 * to get the deeplink [Uri] it has been opened with.
 */
public val ExtrasContainer.deeplinkUri: Uri?
    get() = if (hasExtras()) {
        extras.getParcelable(DEEPLINK_URI, Uri::class.java)
    } else {
        null
    }

internal const val DEEPLINK_URI = "space.octaone.alcubierre.DEEPLINK_URI"
