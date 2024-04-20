package com.github.octaone.alcubierre

import android.os.Bundle
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.reduce.NavReducer
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.AnyRootNavState
import com.github.octaone.alcubierre.state.RootNavState
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

/**
 * Base interface of navigation entrypoint.
 *
 * Library consists of 3 base classes:
 *  - Reducer: maps [RootNavState] according to [NavAction]
 *  - Render: translate commands to android framework (e.g. FragmentManager) for showing specific [RootNavState]
 *  - NavDriveOwner: Base navigation class and Render for [RootNavState] which also stores screens state
 *
 * [state] - current navigation state
 * [dispatch] - method for [NavAction] application
 */
interface NavDrive<S : Screen, D : Dialog> {

    /**
     * Current navigation state.
     */
    val state: RootNavState<S, D>

    /**
     * Dispatch new navigation action.
     *
     * A few notes on the behavior of the default implementation:
     * [dispatch] should be called on the main thread.
     * [dispatch] can be called any time regardless of the application lifecycle.
     * This means that if actions are dispatched after onSavedInstanceState,
     * they will not be saved in case of process death. (similar to the behavior of commitAllowStateLoss).
     * This is usually not a problem if the navigation events come from the UI.
     * But you can alter this behavior by implementing custom lifecycle-aware NavDriveOwner
     * or NavDrive extensions, eg 'forwardDisallowStateLoss'.
     */
    fun dispatch(action: NavAction<S, D>)
}

/**
 * Base interface of navigation owner.
 */
interface NavDriveOwner<S : Screen, D : Dialog> : NavDrive<S, D> {

    /**
     * Observable navigation state.
     */
    val stateFlow: StateFlow<RootNavState<S, D>>

    /**
     * NavDriveOwner initialization. Should be called before any [dispatch] methods.
     * @param reducer Head of the reducers linked list. All actions will be passed to this reducer.
     * @param initialState Initial state of navigation. Will be shown if there is no restored state.
     * @param extras Map with additional parameters. Supported parameters depend on specific implementation.
     */
    fun initialize(
        reducer: NavReducer<AnyRootNavState>,
        initialState: RootNavState<S, D>,
        extras: Map<KClass<*>, Any> = emptyMap()
    )

    /**
     * Should not be called directly by user's code.
     * This method will be invoked when the dialog is dismissed by user interaction and not by changing NavDrive state.
     * Implementations should update their state without any interceptions.
     */
    fun requestDismissDialog()

    /**
     * Saving current NavDrive state.
     */
    fun saveState(outState: Bundle)

    /**
     * Restoring saved NavDrive state.
     */
    fun restoreState(savedState: Bundle?)
}
