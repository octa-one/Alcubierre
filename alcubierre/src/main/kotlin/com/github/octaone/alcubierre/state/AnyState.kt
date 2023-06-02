package com.github.octaone.alcubierre.state

import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

typealias AnyRootNavState = RootNavState<Screen, Dialog>
typealias AnyStackNavState = StackNavState<Screen>
typealias AnyDialogNavState = DialogNavState<Dialog>
