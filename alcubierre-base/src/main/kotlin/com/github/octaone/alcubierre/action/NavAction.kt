package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.RootNavState

/**
 * Base interface for navigation actions.
 * Action can describe any transformation for [RootNavState].
 */
public interface NavAction<out S : Screen, out D : Dialog>

public typealias AnyNavAction = NavAction<Screen, Dialog>
