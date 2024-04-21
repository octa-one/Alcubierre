package com.github.octaone.alcubierre.state

import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

public typealias AnyRootNavState = RootNavState<Screen, Dialog>
public typealias AnyStackNavState = StackNavState<Screen>
public typealias AnyDialogNavState = DialogNavState<Dialog>
