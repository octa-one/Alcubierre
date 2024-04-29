@file:OptIn(AlcubierreInternalApi::class)

package com.github.octaone.alcubierre.screen

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.base.util.getParcelableCompat

/**
 * Return [Lazy] with [FragmentScreen] from which the fragment was created.
 * Used to access [Screen] parameters from a [Fragment], similar to Safe Args in Jetpack Navigation.
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
 * Used to access [Dialog] parameters from a [DialogFragment], similar to Safe Args in Jetpack Navigation.
 * @see Fragment.screenData for code example.
 */
public inline fun <reified T : FragmentDialog> DialogFragment.dialogData(): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        requireNotNull(requireArguments().getParcelableCompat(ARG_DIALOG))
    }

/**
 * Puts [Screen] to arguments bundle.
 */
internal fun Fragment.withScreenData(screen: FragmentScreen): Fragment {
    val bundle = arguments ?: Bundle()
    bundle.putParcelable(ARG_SCREEN, screen)
    arguments = bundle
    return this
}

/**
 * Puts [Dialog] to arguments bundle.
 */
internal fun DialogFragment.withDialogData(dialog: FragmentDialog): DialogFragment {
    val bundle = arguments ?: Bundle()
    bundle.putParcelable(ARG_DIALOG, dialog)
    arguments = bundle
    return this
}

@PublishedApi internal const val ARG_SCREEN: String = "alcubierre_screen"
@PublishedApi internal const val ARG_DIALOG: String = "alcubierre_dialog"
