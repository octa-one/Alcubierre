package space.octaone.alcubierre.base.action

import space.octaone.alcubierre.base.screen.Dialog
import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.base.state.RootNavState

/**
 * Base interface for navigation actions.
 * Action can describe any transformation for [RootNavState].
 */
public interface NavAction<out S : Screen, out D : Dialog>

public typealias AnyNavAction = NavAction<Screen, Dialog>
