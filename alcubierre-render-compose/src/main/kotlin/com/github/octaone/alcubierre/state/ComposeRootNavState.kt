package com.github.octaone.alcubierre.state

import com.github.octaone.alcubierre.screen.ComposeDialog
import com.github.octaone.alcubierre.screen.ComposeScreen

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
