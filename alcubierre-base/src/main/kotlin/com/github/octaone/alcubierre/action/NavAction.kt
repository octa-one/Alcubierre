package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

public interface NavAction<out S : Screen, out D : Dialog>

public typealias AnyNavAction = NavAction<Screen, Dialog>
