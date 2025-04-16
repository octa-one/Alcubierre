/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.octaone.alcubierre.action

import android.net.Uri
import space.octaone.alcubierre.core.NavDrive
import space.octaone.alcubierre.core.action.NavAction
import space.octaone.alcubierre.core.screen.Dialog
import space.octaone.alcubierre.core.screen.Screen
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
