package com.github.octaone.alcubierre

import android.view.View
import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.fragment.host.R

/**
 * Расширение для поиска [Alcubierre] в иерархии фрагментов.
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
    throw IllegalStateException("NavDrive not found")
}

/**
 * Расширение для поиска [Alcubierre] в иерархии View.
 */
fun View.findNavDrive(): NavDrive {
    var view: View? = this

    while (view != null) {
        val tag = view.getTag(R.id.alcubierre_view_tag)
        if (tag is NavDrive) {
            return tag
        } else {
            view = view.parent as? View
        }
    }

    throw IllegalStateException("NavDrive not found")
}
