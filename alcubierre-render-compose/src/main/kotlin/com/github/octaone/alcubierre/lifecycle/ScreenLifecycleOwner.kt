package com.github.octaone.alcubierre.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner

/**
 * As stated in documentation for Lifecycle handling in Compose,
 * you cannot use `LifecycleEventEffect` to listen for Lifecycle.Event.ON_DESTROY
 * since composition ends before this signal is sent.
 *
 * That's why this class exists.
 * @property isFinishing will be set to false if the screen is destroyed, so you can check this at ON_STOP.
 * The idea is similar to `Activity.isFinishing` flag.
 */
public interface ScreenLifecycleOwner : LifecycleOwner {

    /**
     * Whether the screen is in the process of finishing and will be destroyed after ON_STOP.
     */
    public val isFinishing: Boolean
}

/**
 * Wrapper for [LocalLifecycleOwner] to get [ScreenLifecycleOwner] instead of a regular LifecycleOwner.
 */
public object LocalScreenLifecycleOwner {

    public val current: ScreenLifecycleOwner
        @ReadOnlyComposable
        @Composable
        get() = requireNotNull(LocalLifecycleOwner.current as? ScreenLifecycleOwner) { "No ScreenLifecycleOwner provided" }
}
