package com.github.octaone.alcubierre.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner

public interface ScreenLifecycleOwner : LifecycleOwner {

    public val isFinishing: Boolean
}

public object LocalScreenLifecycleOwner {

    public val current: ScreenLifecycleOwner
        @ReadOnlyComposable
        @Composable
        get() = requireNotNull(LocalLifecycleOwner.current as? ScreenLifecycleOwner) { "No ScreenLifecycleOwner provided" }
}
