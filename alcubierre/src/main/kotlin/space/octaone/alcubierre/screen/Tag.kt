package space.octaone.alcubierre.screen

import space.octaone.alcubierre.action.findScreenByTag
import space.octaone.alcubierre.base.screen.extra.ExtrasContainer

/**
 * A simple extension to associate a screen or dialog with a string as a tag.
 * Can be used to find a screen in the backstack.
 * @see findScreenByTag
 */
public var ExtrasContainer.tag: String?
    get() = if (hasExtras()) {
        extras.getString(TAG)
    } else {
        null
    }
    set(value) {
        if (value != null) {
            extras.putString(TAG, value)
        } else {
            extras.remove(TAG)
        }
    }

private const val TAG = "space.octaone.alcubierre.TAG"
