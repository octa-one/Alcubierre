package com.github.octaone.alcubierre.screen

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.util.requireParcelableCompat

inline fun <reified T : FragmentScreen> Fragment.screenData(): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        requireArguments().requireParcelableCompat(ARG_SCREEN)
    }

inline fun <reified T : FragmentDialog> DialogFragment.dialogData(): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE) {
        requireArguments().requireParcelableCompat(ARG_DIALOG)
    }

internal fun Fragment.withScreenData(screen: FragmentScreen): Fragment =
    apply {
        arguments = Bundle().apply { putParcelable(ARG_SCREEN, screen) }
    }

internal fun Fragment.withDialogData(dialog: FragmentDialog): DialogFragment {
    this as DialogFragment
    arguments = Bundle().apply { putParcelable(ARG_DIALOG, dialog) }
    return this
}

@PublishedApi internal const val ARG_SCREEN = "alcubierre_screen"
@PublishedApi internal const val ARG_DIALOG = "alcubierre_dialog"
