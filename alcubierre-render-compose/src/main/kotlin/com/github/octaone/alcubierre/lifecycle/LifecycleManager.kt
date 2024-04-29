package com.github.octaone.alcubierre.lifecycle

import android.app.Application
import android.os.Bundle
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.lifecycle.Lifecycle

/**
 * A basic interface for managing the lifecycle of a screen or dialog.
 */
public interface LifecycleManager {

    /**
     * Unique key of this destination.
     */
    public val key: String

    /**
     * Array of composition locals to be provided with [CompositionLocalProvider].
     */
    public val providedValues: Array<ProvidedValue<*>>

    /**
     * Initialization of the LifecycleManager.
     * Can be called multiple times with the same values due to recompositions.
     */
    public fun initialize(application: Application, savedState: Bundle)

    /**
     * Called when this screen or dialog enters a composition.
     * [parentLifecycleState] is the current state of the parent's lifecycle (eg host Activity).
     */
    public fun onLaunched(parentLifecycleState: Lifecycle.State)

    /**
     * Called before this screen or dialog exits a composition.
     * For screens in the backstack, it will also be called if the screen no longer exists.
     */
    public fun onRemoved()

    /**
     * Called when the parent's lifecycle state changes.
     */
    public fun onParentLifecycleStateChanged(state: Lifecycle.State)
}

/**
 * An interface for managing the lifecycle of a screen.
 * Adds support for handling transition state and backstack push event.
 */
public interface ScreenLifecycleManager : LifecycleManager {

    /**
     * Called when the screen is no longer visible, but it CAN remain in the backstack.
     * Note that the current implementation calls this method even if the screen has been removed.
     * In this case, [onStacked] will be called following [onRemoved].
     */
    public fun onStacked()

    /**
     * Called on the target screen when the transition is complete.
     * After this call, the target screen becomes fully visible.
     */
    public fun onEnterTransitionFinished()

    /**
     * Called on the previous screen when the transition is complete.
     * After this call, the previous screen is no longer visible.
     */
    public fun onExitTransitionFinished()
}

/**
 * An interface for managing the lifecycle of a dialog.
 */
public typealias DialogLifecycleManager = LifecycleManager
