package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

/**
 * [Screen] implementation using fragments
 */
abstract class FragmentScreen(
    val fragmentName: String,
    val replace: Boolean = true
) : Screen(), ExtrasContainer by LazyExtrasContainer() {

    constructor(
        fragmentClass: KClass<out Fragment>,
        replace: Boolean = true
    ) : this(fragmentClass.java.name, replace)
}
