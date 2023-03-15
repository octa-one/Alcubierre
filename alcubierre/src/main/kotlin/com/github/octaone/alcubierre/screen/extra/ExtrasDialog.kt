package com.github.octaone.alcubierre.screen.extra

import com.github.octaone.alcubierre.ExtrasKeys
import com.github.octaone.alcubierre.screen.Dialog

var Dialog.isShowing: Boolean
    get() = containsKey(ExtrasKeys.DIALOG_SHOWING)
    set(value) { putBoolean(ExtrasKeys.DIALOG_SHOWING, value) }
