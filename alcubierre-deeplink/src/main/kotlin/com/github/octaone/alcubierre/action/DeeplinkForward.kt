package com.github.octaone.alcubierre.action

import android.net.Uri
import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

class DeeplinkForward<S : Screen, D : Dialog>(val deeplink: Uri) : NavAction<S, D>

fun <S : Screen, D : Dialog> NavDrive<S, D>.forward(deeplink: String) = forward(Uri.parse(deeplink))
fun <S : Screen, D : Dialog> NavDrive<S, D>.forward(deeplink: Uri) = dispatch(DeeplinkForward(deeplink))
