package com.github.octaone.alcubierre.screen

import com.github.octaone.alcubierre.ExtrasKeys

var Extras.tag: String?
    get() = if (hasExtras()) extras.getString(ExtrasKeys.TAG) else null
    set(value) { extras.putString(ExtrasKeys.TAG, value) }
