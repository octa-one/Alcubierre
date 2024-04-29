@file:Suppress("LeakingThis")

package com.github.octaone.alcubierre.screen

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.github.octaone.alcubierre.lifecycle.DefaultScreenLifecycleManager
import com.github.octaone.alcubierre.lifecycle.ScreenLifecycleManager
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

/**
 * [Screen] implementation for Compose.
 *
 * @param composeContentName The fully qualified name of the [ComposeScreenContent] class.
 * @param composeContentClass The class of the [ComposeScreenContent], if it exists, matches [composeContentName].
 *
 * In some app architectures the [Screen] classes used for navigation may be separate from its implementation ([ComposeScreenContent]).
 * In such case, you can pass a class name or a class of the content.
 * Once navigated, the content will be created using the default constructor via reflection.
 * The same idea is used in FragmentFactory.
 *
 * But if the screen and content are placed in the same module, it's better to combine them into one class.
 * For example:
 * ```
 * class SomeScreen : ComposeScreen(), ComposeScreenContent<SomeScreen> () {
 *
 *     @Composable
 *     fun SomeScreen.Content() { }
 * }
 * ```
 * Each screen has its own [lifecycleManager], the implementation can be overridden by child classes.
 * The default implementation consists of [LifecycleOwner], [ViewModelStoreOwner], [SavedStateRegistryOwner].
 */
@Stable
public abstract class ComposeScreen(
    public val composeContentName: String,
    public val composeContentClass: Class<out ComposeScreenContent<*>>?
) : Screen(), ExtrasContainer by LazyExtrasContainer() {

    public constructor() : this("", null) { require(this is ComposeScreenContent<*>) }
    public constructor(contentName: String) : this(contentName, null)
    public constructor(contentClass: KClass<out ComposeScreenContent<*>>) : this(contentClass.java.name, contentClass.java)

    internal var content: ComposeScreenContent<*>? = null

    /**
     * A manager for handling the screen lifecycle.
     * @see DefaultScreenLifecycleManager
     */
    public open val lifecycleManager: ScreenLifecycleManager by lazy(LazyThreadSafetyMode.NONE) {
        DefaultScreenLifecycleManager(screenId, getSavedStateDefaultArguments())
    }

    /**
     * Default arguments that should be passed to SavedStateHandle.
     * For example, you can return arguments of a screen constructor for later use in a ViewModel.
     */
    public open fun getSavedStateDefaultArguments(): Bundle? = null
}

/**
 * Separate interface for classes containing composable contents.
 * It is separate from [ComposeScreen] because in some app architectures
 * the [Screen] classes used for navigation may be separate from its implementation.
 * If this is not the case, you can implement this interface in the [Screen] class.
 */
public interface ComposeScreenContent<S : ComposeScreen> {

    /**
     * Content of the [ComposeScreen].
     */
    @Composable
    public fun S.Content()
}
