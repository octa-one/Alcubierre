@file:OptIn(AlcubierreFragmentNameConstructor::class)

package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.octaone.alcubierre.annotation.AlcubierreFragmentNameConstructor

/**
 * [Screen] implementation based on [Fragment].
 * @param fragmentName Fully qualified name class of fragment, required for [FragmentFactory].
 * @param replace Transaction to commit, replace or add.
 *
 * @see FragmentCreator for manual Fragment instantiation.
 */
public abstract class FragmentNameScreen(
    fragmentName: String,
    replace: Boolean = true
) : FragmentScreen(fragmentName, replace)
