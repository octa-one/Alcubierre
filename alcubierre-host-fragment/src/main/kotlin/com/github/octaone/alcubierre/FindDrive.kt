package com.github.octaone.alcubierre

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment

/**
 * Extension for searching [NavDrive] in fragments hierarchy.
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
 */
public fun View.findNavDrive(): FragmentNavDrive {
    val fragment = runCatching { findFragment<Fragment>() }
        .getOrElse { navDriveNotFoundError() }

    return fragment.findNavDrive()
}

private fun navDriveNotFoundError(): Nothing =
    throw IllegalStateException("NavDrive not found")
