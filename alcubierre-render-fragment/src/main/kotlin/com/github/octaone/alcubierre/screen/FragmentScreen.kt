package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.screen.extra.LazyBundleExtras
import com.github.octaone.alcubierre.screen.extra.ParcelableExtras
import kotlin.reflect.KClass

/**
 * [Screen] implementation using fragments
 */
abstract class FragmentScreen(
    val fragmentName: String,
    val replace: Boolean = true
) : Screen, ParcelableExtras by LazyBundleExtras() {

    override val screenId: String by lazy(LazyThreadSafetyMode.NONE) { "${fragmentName}_${hashCode()}" }

    constructor(
        fragmentClass: KClass<out Fragment>,
        replace: Boolean = true
    ) : this(fragmentClass.java.name, replace)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FragmentScreen

        if (fragmentName != other.fragmentName) return false

        return true
    }

    override fun hashCode(): Int = fragmentName.hashCode()
}
