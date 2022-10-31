package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/**
 * Реализация [Screen] с использованием фрагментов.
 */
abstract class FragmentScreen(
    val fragmentName: String,
    val replace: Boolean = true
) : Screen {

    constructor(
        fragmentClass: KClass<out Fragment>,
        replace: Boolean = true
    ) : this(fragmentClass.java.name, replace)

    override val id: String by lazy(LazyThreadSafetyMode.NONE) { fragmentName + hashCode() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FragmentScreen

        if (fragmentName != other.fragmentName) return false

        return true
    }

    override fun hashCode(): Int = fragmentName.hashCode()
}
