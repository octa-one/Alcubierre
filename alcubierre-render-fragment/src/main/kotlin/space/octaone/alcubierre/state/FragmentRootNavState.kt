package space.octaone.alcubierre.state

import space.octaone.alcubierre.base.state.DialogNavState
import space.octaone.alcubierre.base.state.RootNavState
import space.octaone.alcubierre.base.state.StackNavState
import space.octaone.alcubierre.screen.FragmentDialog
import space.octaone.alcubierre.screen.FragmentScreen

/**
 * [RootNavState] for fragment navigation.
 */
public typealias FragmentRootNavState = RootNavState<FragmentScreen, FragmentDialog>

/**
 * [StackNavState] for fragment navigation.
 */
public typealias FragmentStackNavState = StackNavState<FragmentScreen>

/**
 * [DialogNavState] for fragment navigation.
 */
public typealias FragmentDialogNavState = DialogNavState<FragmentDialog>
