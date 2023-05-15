package com.github.octaone.alcubierre.screen

import androidx.fragment.app.DialogFragment
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

/**
 * [Dialog] implementation using fragments
 */
abstract class FragmentDialog(
    val fragmentName: String
) : Dialog(), ExtrasContainer by LazyExtrasContainer() {

    override val priority: Int = 5

    constructor(
        fragmentClass: KClass<out DialogFragment>
    ) : this(fragmentClass.java.name)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FragmentScreen

        if (fragmentName != other.fragmentName) return false

        return true
    }

    override fun hashCode(): Int = fragmentName.hashCode()
}
