package com.github.octaone.alcubierre.screen

import com.github.octaone.alcubierre.screen.extra.ExtrasContainer

/**
 * A simple extension to associate a screen or dialog with a string as a tag.
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

private const val TAG = "com.github.octaone.alcubierre.TAG"
