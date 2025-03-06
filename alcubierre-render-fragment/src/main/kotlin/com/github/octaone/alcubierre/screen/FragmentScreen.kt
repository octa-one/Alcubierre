@file:OptIn(AlcubierreFragmentNameConstructor::class)

package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.octaone.alcubierre.annotation.AlcubierreFragmentNameConstructor
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

/**
 * [Screen] implementation based on [Fragment].
 * @param fragmentName Class of the fragment representing the screen.
 * @param replace Transaction to commit, replace or add.
 *
 * Android uses fragment names to instantiate fragments (see [FragmentFactory.instantiate]).
 * To prevent failures due to typos in fragment names,
 * the default constructor is annotated with [AlcubierreFragmentNameConstructor] prohibiting its use.
 * The safest way would be to use a class reference using a secondary constructor.
 *
 * In some multi-module architectures you may not have a class reference,
 * in that case use the `FragmentNameDialog` from module `alcubierre-render-fragment-screen-reflect`.
 * It allows to create a [FragmentScreen] with just a String name.
 *
 *
 * @see FragmentCreator for manual Fragment instantiation.
 */
public abstract class FragmentScreen @AlcubierreFragmentNameConstructor constructor(
    public val fragmentName: String,
    public val replace: Boolean
) : Screen(), ExtrasContainer by LazyExtrasContainer() {

    public constructor(
        fragmentClass: KClass<out Fragment>,
        replace: Boolean = true
    ) : this(fragmentClass.java.name, replace)
}
