package com.github.octaone.alcubierre.action

import android.net.Uri
import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.deeplink.DeeplinkResolver
import com.github.octaone.alcubierre.reducer.DeeplinkReducer
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

/**
 * Action to navigate to the deeplink Uri.
 * @see DeeplinkResolver
 * @see DeeplinkReducer
 */
public class DeeplinkForward<S : Screen, D : Dialog>(public val deeplink: Uri) : NavAction<S, D>

/**
 * Extension to dispatch action [DeeplinkForward].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.forward(deeplink: String): Unit = forward(Uri.parse(deeplink))

/**
 * Extension to dispatch action [DeeplinkForward].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.forward(deeplink: Uri): Unit = dispatch(DeeplinkForward(deeplink))
