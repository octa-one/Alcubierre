@file:Suppress("LeakingThis")

package com.github.octaone.alcubierre.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
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
public abstract class ComposeNameScreen(
    public val composeContentName: String?,
    public val composeContentClass: Class<out ComposeScreenContent<*>>?
) : ComposeScreen() {

    public constructor(contentName: String) : this(contentName, null)
    public constructor(contentClass: KClass<out ComposeScreenContent<*>>) : this(null, contentClass.java)

    internal var content: ComposeScreenContent<*>? = null

    @Composable
    final override fun Content() {
        val classLoader = LocalContext.current.classLoader
        getContent(classLoader).GenericContent(this)
    }
}

/**
 * Separate interface for classes containing composable contents.
 * It is separate from [ComposeScreen] because in some app architectures
 * the [Screen] classes used for navigation may be separate from its implementation.
 * If this is not the case, you can implement this interface in the [Screen] class.
 */
public abstract class ComposeScreenContent<S : ComposeScreen> {

    /**
     * Content of the [ComposeScreen].
     */
    @Composable
    public abstract fun Content(screen: S)
}
