package com.github.octaone.alcubierre.state

import com.github.octaone.alcubierre.screen.FragmentDialog
import com.github.octaone.alcubierre.screen.FragmentScreen

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
