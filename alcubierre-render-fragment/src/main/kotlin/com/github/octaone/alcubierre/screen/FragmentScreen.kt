package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

/**
 * [Screen] implementation based on [Fragment].
 * @param fragmentName Fully qualified name class of fragment, required for [FragmentFactory].
 * @param replace Transaction to commit, replace or add.
 *
 * @see FragmentCreator for manual Fragment instantiation.
 */
public abstract class FragmentScreen(
    public val fragmentName: String,
    public val replace: Boolean = true
) : Screen(), ExtrasContainer by LazyExtrasContainer() {

    public constructor(
        fragmentClass: KClass<out Fragment>,
        replace: Boolean = true
    ) : this(fragmentClass.java.name, replace)
}
