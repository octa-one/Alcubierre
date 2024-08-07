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
public abstract class ComposeScreen : Screen(), ExtrasContainer by LazyExtrasContainer() {

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

    @Composable
    public abstract fun Content()
}

