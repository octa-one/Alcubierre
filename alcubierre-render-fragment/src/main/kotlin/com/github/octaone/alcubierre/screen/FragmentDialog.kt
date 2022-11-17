package com.github.octaone.alcubierre.screen

import androidx.fragment.app.DialogFragment
import kotlin.reflect.KClass

/**
 * [Dialog] implementation using fragments
 */
abstract class FragmentDialog(
    val fragmentName: String
) : Dialog {

    constructor(
        fragmentClass: KClass<out DialogFragment>
    ) : this(fragmentClass.java.name)

    override val dialogId: String by lazy(LazyThreadSafetyMode.NONE) { "${fragmentName}_${hashCode()}" }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FragmentScreen

        if (fragmentName != other.fragmentName) return false

        return true
    }

    override fun hashCode(): Int = fragmentName.hashCode()
}
