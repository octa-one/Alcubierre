package com.github.octaone.alcubierre.screen

import com.github.octaone.alcubierre.ExtrasKeys
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer

var ExtrasContainer.tag: String?
    get() = if (hasExtras()) {
        extras.getString(ExtrasKeys.TAG)
    } else {
        null
    }
    set(value) {
        if (value != null) {
            extras.putString(ExtrasKeys.TAG, value)
        } else {
            extras.remove(ExtrasKeys.TAG)
        }
    }
