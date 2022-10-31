package com.github.octaone.alcubierre.screen

import androidx.fragment.app.DialogFragment
import kotlin.reflect.KClass

/**
 * Реализация [Dialog] с использованием фрагментов.
 */
abstract class FragmentDialog(
    val fragmentName: String
) : Dialog {

    constructor(fragmentClass: KClass<out DialogFragment>) : this(fragmentClass.java.name)

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
