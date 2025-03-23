package space.octaone.alcubierre.action

import android.net.Uri
import space.octaone.alcubierre.base.NavDrive
import space.octaone.alcubierre.base.action.NavAction
import space.octaone.alcubierre.base.screen.Dialog
import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.deeplink.DeeplinkResolver
import space.octaone.alcubierre.reducer.DeeplinkReducer

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
