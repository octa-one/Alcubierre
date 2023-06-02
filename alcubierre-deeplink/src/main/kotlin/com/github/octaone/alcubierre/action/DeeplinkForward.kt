package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

class DeeplinkForward<S : Screen, D : Dialog>(val deeplink: String) : NavAction<S, D>

fun <S : Screen, D : Dialog> NavDrive<S, D>.forward(deeplink: String) = dispatch(DeeplinkForward(deeplink))
