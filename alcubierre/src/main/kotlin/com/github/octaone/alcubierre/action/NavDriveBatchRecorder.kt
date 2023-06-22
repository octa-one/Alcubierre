package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.RootNavState

class NavDriveBatchRecorder<S : Screen, D : Dialog> (
    initialState: RootNavState<S, D>
) : NavDrive<S, D> {

    override val state: RootNavState<S, D> = initialState

    val actions = mutableListOf<NavAction<S, D>>()

    override fun dispatch(action: NavAction<S, D>) {
        actions.add(action)
    }
}
