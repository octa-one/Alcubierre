package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.NavDrive

class DeeplinkForward(val deeplink: String) : NavAction

fun NavDrive.forward(deeplink: String) = dispatch(DeeplinkForward(deeplink))
