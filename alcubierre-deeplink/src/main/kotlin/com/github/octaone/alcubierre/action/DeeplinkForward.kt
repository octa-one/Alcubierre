package com.github.octaone.alcubierre.action

import android.net.Uri
import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

public class DeeplinkForward<S : Screen, D : Dialog>(public val deeplink: Uri) : NavAction<S, D>

public fun <S : Screen, D : Dialog> NavDrive<S, D>.forward(deeplink: String): Unit = forward(Uri.parse(deeplink))
public fun <S : Screen, D : Dialog> NavDrive<S, D>.forward(deeplink: Uri): Unit = dispatch(DeeplinkForward(deeplink))
