package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/**
 * [Screen] implementation using fragments
 */
abstract class FragmentScreen(
    val fragmentName: String,
    val replace: Boolean = true
) : Screen, Extras by ExtrasLazyImpl() {

    constructor(
        fragmentClass: KClass<out Fragment>,
        replace: Boolean = true
    ) : this(fragmentClass.java.name, replace)

    override val screenId: String by lazy(LazyThreadSafetyMode.NONE) { "${fragmentName}_${hashCode()}" }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FragmentScreen

        if (fragmentName != other.fragmentName) return false

        return true
    }

    override fun hashCode(): Int = fragmentName.hashCode()
}
