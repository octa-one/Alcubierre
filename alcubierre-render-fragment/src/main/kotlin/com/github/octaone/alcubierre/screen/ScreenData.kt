@file:OptIn(AlcubierreInternalApi::class)

package com.github.octaone.alcubierre.screen

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.base.util.getParcelableCompat

inline fun <reified T : FragmentScreen> Fragment.screenData(): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        requireNotNull(requireArguments().getParcelableCompat(ARG_SCREEN))
    }

inline fun <reified T : FragmentDialog> DialogFragment.dialogData(): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        requireNotNull(requireArguments().getParcelableCompat(ARG_DIALOG))
    }

internal fun Fragment.withScreenData(screen: FragmentScreen): Fragment {
    val bundle = arguments ?: Bundle()
    bundle.putParcelable(ARG_SCREEN, screen)
    arguments = bundle
    return this
}

internal fun DialogFragment.withDialogData(dialog: FragmentDialog): DialogFragment {
    val bundle = arguments ?: Bundle()
    bundle.putParcelable(ARG_DIALOG, dialog)
    arguments = bundle
    return this
}

@PublishedApi internal const val ARG_SCREEN = "alcubierre_screen"
@PublishedApi internal const val ARG_DIALOG = "alcubierre_dialog"
