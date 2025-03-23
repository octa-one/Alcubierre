package space.octaone.alcubierre.state

import space.octaone.alcubierre.base.state.DialogNavState
import space.octaone.alcubierre.base.state.RootNavState
import space.octaone.alcubierre.base.state.StackNavState
import space.octaone.alcubierre.screen.ComposeDialog
import space.octaone.alcubierre.screen.ComposeScreen

/**
 * [RootNavState] for Compose navigation.
 */
public typealias ComposeRootNavState = RootNavState<ComposeScreen, ComposeDialog>

/**
 * [StackNavState] for Compose navigation.
 */
public typealias ComposeStackNavState = StackNavState<ComposeScreen>

/**
 * [DialogNavState] for Compose navigation.
 */
public typealias ComposeDialogNavState = DialogNavState<ComposeDialog>
