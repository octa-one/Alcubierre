package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

interface NavAction<out S : Screen, out D : Dialog>

typealias AnyNavAction = NavAction<*, *>
