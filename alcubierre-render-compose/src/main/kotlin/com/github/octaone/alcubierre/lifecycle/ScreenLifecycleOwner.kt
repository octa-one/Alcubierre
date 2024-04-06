package com.github.octaone.alcubierre.lifecycle

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.LifecycleOwner

interface ScreenLifecycleOwner : LifecycleOwner {

    val isFinishing: Boolean
}

val LocalScreenLifecycleOwner = staticCompositionLocalOf<ScreenLifecycleOwner> {
    error("No ScreenLifecycleOwner provided")
}
