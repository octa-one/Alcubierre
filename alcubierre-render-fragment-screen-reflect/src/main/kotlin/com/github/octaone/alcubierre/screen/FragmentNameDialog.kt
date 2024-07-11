package com.github.octaone.alcubierre.screen

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentFactory
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

/**
 * [Dialog] implementation based on [DialogFragment].
 * @param fragmentName Fully qualified name class of fragment, required for [FragmentFactory].
 *
 * @see FragmentCreator for manual Fragment instantiation.
 */
public abstract class FragmentNameDialog(
    public val fragmentName: String
) : Dialog(), ExtrasContainer by LazyExtrasContainer() {

    /**
     * Priority of the dialog in the queue.
     * Priority handling depends on the implementation of the reducer.
     * The default implementation assumes that the visible dialog has the highest priority.
     */
    override val priority: Int = DEFAULT_PRIORITY

    public constructor(
        fragmentClass: KClass<out DialogFragment>
    ) : this(fragmentClass.java.name)
}

private const val DEFAULT_PRIORITY = 5
