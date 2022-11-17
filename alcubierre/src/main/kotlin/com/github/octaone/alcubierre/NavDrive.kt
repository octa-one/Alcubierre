package com.github.octaone.alcubierre

import android.os.Bundle
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.state.RootNavState

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

    fun onResume()

    fun onPause()

    fun saveState(outState: Bundle)
}
