package com.github.octaone.alcubierre

import android.os.Bundle
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.reduce.NavReducer
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
interface NavDrive {

    val state: RootNavState

    fun dispatch(action: NavAction)
}

/**
 * Base interface of navigation host
 */
interface NavDriveOwner : NavDrive {

    val stateFlow: StateFlow<RootNavState>

    fun initialize(
        reducer: NavReducer<RootNavState>,
        initialState: RootNavState,
        extras: Map<KClass<*>, Any> = emptyMap()
    )

    fun requestDismissDialog()

    fun saveState(outState: Bundle)

    fun restoreState(savedState: Bundle?)
}
