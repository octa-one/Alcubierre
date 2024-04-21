package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment

/**
 * Extension for [FragmentScreen] for specific fragment creation (e.g. fragment from third-party library)
 */
public interface FragmentCreator {

    public fun create(): Fragment
}
