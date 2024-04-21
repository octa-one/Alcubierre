package com.github.octaone.alcubierre.screen

import androidx.fragment.app.DialogFragment
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

/**
 * [Dialog] implementation using fragments
 */
public abstract class FragmentDialog(
    public val fragmentName: String
) : Dialog(), ExtrasContainer by LazyExtrasContainer() {

    override val priority: Int = 5

    public constructor(
        fragmentClass: KClass<out DialogFragment>
    ) : this(fragmentClass.java.name)
}
