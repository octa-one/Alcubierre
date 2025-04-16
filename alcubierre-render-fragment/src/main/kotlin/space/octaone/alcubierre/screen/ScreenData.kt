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

@file:OptIn(AlcubierreInternalApi::class)

package space.octaone.alcubierre.screen

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import space.octaone.alcubierre.core.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.core.util.getParcelableCompat

/**
 * Return [Lazy] with [FragmentScreen] from which the fragment was created.
 * Used to access [space.octaone.alcubierre.core.screen.Screen] parameters from a [Fragment], similar to Safe Args in Jetpack Navigation.
 * For example:
 * ```
 * class SomeScreen(val title: String, val id: Int) : FragmentScreen(...)
 *
 * class SomeFragment : Fragment() {
 *
 *     private val screen: SomeScreen by screenData()
 *
 *     override fun onViewCreated(...) {
 *          binding.toolbar.setTitle(screen.title)
 *          ...
 *     }
 * }
 * ```
 */
public inline fun <reified T : FragmentScreen> Fragment.screenData(): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        requireNotNull(requireArguments().getParcelableCompat(ARG_SCREEN))
    }

/**
 * Return [Lazy] with [FragmentDialog] from which the fragment was created.
 * Used to access [space.octaone.alcubierre.core.screen.Dialog] parameters from a [DialogFragment], similar to Safe Args in Jetpack Navigation.
 * @see Fragment.screenData for code example.
 */
public inline fun <reified T : FragmentDialog> DialogFragment.dialogData(): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        requireNotNull(requireArguments().getParcelableCompat(ARG_DIALOG))
    }

/**
 * Puts [space.octaone.alcubierre.core.screen.Screen] to arguments bundle.
 */
internal fun Fragment.withScreenData(screen: FragmentScreen): Fragment {
    val bundle = arguments ?: Bundle()
    bundle.putParcelable(ARG_SCREEN, screen)
    arguments = bundle
    return this
}

/**
 * Puts [space.octaone.alcubierre.core.screen.Dialog] to arguments bundle.
 */
internal fun DialogFragment.withDialogData(dialog: FragmentDialog): DialogFragment {
    val bundle = arguments ?: Bundle()
    bundle.putParcelable(ARG_DIALOG, dialog)
    arguments = bundle
    return this
}

@PublishedApi internal const val ARG_SCREEN: String = "alcubierre_screen"
@PublishedApi internal const val ARG_DIALOG: String = "alcubierre_dialog"
