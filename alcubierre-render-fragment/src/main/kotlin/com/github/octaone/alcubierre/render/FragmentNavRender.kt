package com.github.octaone.alcubierre.render

import android.os.Bundle
import android.os.Parcelable

/**
 * Base interface for any FragmentManager bases renders.
 *
 * Renders have their own state, because host state can not be rendered immediately.
 * For example, the states will be different when the application is in the background.
 */
interface FragmentNavRender<T : Parcelable> {

    /**
     * Render functions. Issuing commands to FragmentManager to represent [state].
     */
    fun render(state: T)

    /**
     * Saving current render's state.
     */
    fun saveState(outState: Bundle)

    /**
     * Restoring last render's state from "savedInstanceState" bundle.
     */
    fun restoreState(savedState: Bundle?)
}
