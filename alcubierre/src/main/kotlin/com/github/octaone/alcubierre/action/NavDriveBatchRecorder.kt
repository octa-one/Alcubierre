package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.state.RootNavState

class NavDriveBatchRecorder(
    initialState: RootNavState
) : NavDrive {

    override val state: RootNavState = initialState

    val actions = mutableListOf<NavAction>()

    override fun dispatch(action: NavAction) {
        actions.add(action)
    }
}
