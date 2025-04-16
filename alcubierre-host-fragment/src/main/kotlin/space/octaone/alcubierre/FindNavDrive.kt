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

package space.octaone.alcubierre

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import space.octaone.alcubierre.core.NavDrive
import space.octaone.alcubierre.host.AlcubierreNavDriveFragment

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
