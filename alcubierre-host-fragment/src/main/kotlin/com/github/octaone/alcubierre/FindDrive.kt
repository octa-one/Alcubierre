package com.github.octaone.alcubierre

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment

/**
 * Extension for searching [Alcubierre] in fragments hierarchy
 */
fun Fragment.findNavDrive(): NavDrive {
    var findFragment: Fragment? = this
    while (findFragment != null) {
        val primaryNavFragment = findFragment.parentFragmentManager.primaryNavigationFragment
        if (primaryNavFragment is NavDrive) {
            return primaryNavFragment
        }
        findFragment = findFragment.parentFragment
    }
    navDriveNotFoundError()
}

/**
 * Extension for searching [Alcubierre] in View hierarchy
 */
fun View.findNavDrive(): NavDrive {
    val fragment = runCatching { findFragment<Fragment>() }
        .getOrElse { navDriveNotFoundError() }

    return fragment.findNavDrive()
}

private fun navDriveNotFoundError(): Nothing =
    throw IllegalStateException("NavDrive not found")
