package com.github.octaone.alcubierre.screen.extra

import com.github.octaone.alcubierre.ExtrasKeys

var ParcelableExtras.tag: String?
    get() = if (containsKey(ExtrasKeys.TAG)) {
        getString(ExtrasKeys.TAG)
    } else {
        null
    }
    set(value) {
        if (value != null) {
            putString(ExtrasKeys.TAG, value)
        } else {
            remove(ExtrasKeys.TAG)
        }
    }
