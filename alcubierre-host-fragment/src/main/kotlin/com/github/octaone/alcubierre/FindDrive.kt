package com.github.octaone.alcubierre

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.github.octaone.alcubierre.screen.FragmentDialog
import com.github.octaone.alcubierre.screen.FragmentScreen

/**
 * Extension for searching [Alcubierre] in fragments hierarchy
 */
fun Fragment.findNavDrive(): FragmentNavDrive {
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
 * Extension for searching [Alcubierre] in View hierarchy
 */
fun View.findNavDrive(): FragmentNavDrive {
    val fragment = runCatching { findFragment<Fragment>() }
        .getOrElse { navDriveNotFoundError() }

    return fragment.findNavDrive()
}

private fun navDriveNotFoundError(): Nothing =
    throw IllegalStateException("NavDrive not found")
