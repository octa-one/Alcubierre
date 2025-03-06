@file:OptIn(AlcubierreFragmentNameConstructor::class)

package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.annotation.AlcubierreFragmentNameConstructor

/**
 * “Unsafe” version of [FragmentDialog], allowing the fully qualified name to be used to reference the Fragment class.
 *
 * @param fragmentName Fully qualified name of the [Fragment] class.
 *
 * @see FragmentCreator for manual Fragment instantiation.
 */
public abstract class FragmentNameDialog(
    fragmentName: String
) : FragmentDialog(fragmentName)