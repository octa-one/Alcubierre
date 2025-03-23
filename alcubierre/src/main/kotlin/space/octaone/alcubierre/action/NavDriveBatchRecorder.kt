package space.octaone.alcubierre.action

import space.octaone.alcubierre.base.NavDrive
import space.octaone.alcubierre.base.action.NavAction
import space.octaone.alcubierre.base.screen.Dialog
import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.base.state.RootNavState

/**
 * Utility class for recording multiple [space.octaone.alcubierre.base.action.NavAction]s and dispatching them simultaneously.
 */
internal class NavDriveBatchRecorder<S : Screen, D : Dialog> (
    initialState: RootNavState<S, D>
) : NavDrive<S, D> {

    override val state: RootNavState<S, D> = initialState

    val actions = mutableListOf<NavAction<S, D>>()

    override fun dispatch(action: NavAction<S, D>) {
        actions.add(action)
    }
}
