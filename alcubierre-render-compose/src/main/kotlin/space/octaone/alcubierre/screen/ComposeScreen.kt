/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("LeakingThis")

package space.octaone.alcubierre.screen

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import space.octaone.alcubierre.core.screen.Screen
import space.octaone.alcubierre.core.screen.extra.ExtrasContainer
import space.octaone.alcubierre.core.screen.extra.LazyExtrasContainer
import space.octaone.alcubierre.lifecycle.DefaultScreenLifecycleManager
import space.octaone.alcubierre.lifecycle.ScreenLifecycleManager

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

