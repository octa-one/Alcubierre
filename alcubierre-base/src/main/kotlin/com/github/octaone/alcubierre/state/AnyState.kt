package com.github.octaone.alcubierre.state

import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

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
