package com.github.octaone.alcubierre.screen

import com.github.octaone.alcubierre.ExtrasKeys

var Dialog.isShowing: Boolean
    get() = hasExtras() && extras.getBoolean(ExtrasKeys.DIALOG_SHOWING)
    set(value) {
        if (value) {
            extras.putBoolean(ExtrasKeys.DIALOG_SHOWING, true)
        } else {
            extras.remove(ExtrasKeys.DIALOG_SHOWING)
        }
    }
