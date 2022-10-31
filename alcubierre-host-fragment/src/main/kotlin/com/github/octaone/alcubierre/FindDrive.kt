package com.github.octaone.alcubierre

import android.view.View
import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.fragment.host.R

/**
 * Расширение для поиска [Alcubierre] в иерархии фрагментов.
 */
fun Fragment.findAlcubierre(): Alcubierre {
    var findFragment: Fragment? = this
    while (findFragment != null) {
        val primaryNavFragment = findFragment.parentFragmentManager.primaryNavigationFragment
        if (primaryNavFragment is Alcubierre) {
            return primaryNavFragment
        }
        findFragment = findFragment.parentFragment
    }
    throw IllegalStateException("Alcubierre not found")
}

/**
 * Расширение для поиска [Alcubierre] в иерархии View.
 */
fun View.findAlcubierre(): Alcubierre {
    var view: View? = this

    while (view != null) {
        val tag = view.getTag(R.id.alcubierre_view_tag)
        if (tag is Alcubierre) {
            return tag
        } else {
            view = view.parent as? View
        }
    }

    throw IllegalStateException("Alcubierre not found")
}
