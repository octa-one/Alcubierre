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
 * Base interface of navigation
 *
 * Library consists of 3 base classes:
 *  - Reducer: maps [RootNavState] according to [NavAction]
 *  - Render: translate commands to android framework (e.g. FragmentManager) for showing specific [RootNavState]
 *  - AlcubierreHost: Base navigation class and Render for [RootNavState] which also stores screens state
 *
 * [state] - current navigation state
 * [dispatch] - method for [NavAction] application
 */
interface NavDrive<S : Screen, D : Dialog> {

    val state: RootNavState<S, D>

    fun dispatch(action: NavAction<S, D>)
}

/**
 * Base interface of navigation host
 */
interface NavDriveOwner<S : Screen, D : Dialog> : NavDrive<S, D> {

    val stateFlow: StateFlow<RootNavState<S, D>>

    fun initialize(
        reducer: NavReducer<AnyRootNavState>,
        initialState: RootNavState<S, D>,
        extras: Map<KClass<*>, Any> = emptyMap()
    )

    fun requestDismissDialog()

    fun saveState(outState: Bundle)

    fun restoreState(savedState: Bundle?)
}
