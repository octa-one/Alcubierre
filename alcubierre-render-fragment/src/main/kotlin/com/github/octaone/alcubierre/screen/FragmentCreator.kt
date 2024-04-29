package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment

/**
 * Fragments for [FragmentScreen] or [FragmentDialog] are created using FragmentFactory by their class name.
 * But sometimes it is necessary to create fragments manually.
 * For example, if the fragment is from an external library (MapFragment, etc).
 * In such cases, you can implement [FragmentCreator] in [FragmentScreen] or [FragmentDialog].
 * The default render will call it instead of FragmentFactory.
 */
public interface FragmentCreator {

    /**
     * Creates an instance of Fragment associated with [FragmentScreen] or [FragmentDialog].
     */
    public fun create(): Fragment
}
