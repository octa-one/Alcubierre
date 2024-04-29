package com.github.octaone.alcubierre

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.github.octaone.alcubierre.host.AlcubierreNavDriveFragment

/**
 * Extension for searching [NavDrive] in the fragment hierarchy.
 *
 * Calling this on a Fragment that is not a [AlcubierreNavDriveFragment]
 * or within a [AlcubierreNavDriveFragment] will result in an [IllegalStateException].
 */
public fun Fragment.findNavDrive(): FragmentNavDrive {
    var findFragment: Fragment? = this
    while (findFragment != null) {
        val primaryNavFragment = findFragment.parentFragmentManager.primaryNavigationFragment
        if (primaryNavFragment is NavDrive<*, *>) {
            @Suppress("UNCHECKED_CAST")
            return primaryNavFragment as FragmentNavDrive
        }
        findFragment = findFragment.parentFragment
    }
    navDriveNotFoundError()
}

/**
 * Extension for searching [NavDrive] in View hierarchy.
 * Works the same as [Fragment.findNavDrive], but initially finds current fragment for the given [View].
 */
public fun View.findNavDrive(): FragmentNavDrive {
    val fragment = runCatching { findFragment<Fragment>() }
        .getOrElse { navDriveNotFoundError() }

    return fragment.findNavDrive()
}

private fun navDriveNotFoundError(): Nothing =
    throw IllegalStateException("NavDrive not found")
