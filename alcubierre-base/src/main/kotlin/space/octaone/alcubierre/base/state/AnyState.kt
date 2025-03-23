package space.octaone.alcubierre.base.state

import space.octaone.alcubierre.base.screen.Dialog
import space.octaone.alcubierre.base.screen.Screen

/**
 * RootNavState for Screen/Dialog type agnostic components, primarily for NavReducers.
 */
public typealias AnyRootNavState = RootNavState<Screen, Dialog>

/**
 * StackNavState for Screen type agnostic components, primarily for NavReducers.
 */
public typealias AnyStackNavState = StackNavState<Screen>

/**
 * DialogNavState for Dialog type agnostic components, primarily for NavReducers.
 */
public typealias AnyDialogNavState = DialogNavState<Dialog>
