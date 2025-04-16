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

package space.octaone.alcubierre.screen

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentFactory
import space.octaone.alcubierre.annotation.AlcubierreFragmentNameConstructor
import space.octaone.alcubierre.core.screen.Dialog
import space.octaone.alcubierre.core.screen.extra.ExtrasContainer
import space.octaone.alcubierre.core.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

/**
 * [Dialog] implementation based on [DialogFragment].
 * @param fragmentClass Class of the fragment representing the screen.
 *
 * Android uses fragment names to instantiate fragments (see [FragmentFactory.instantiate]).
 * To prevent failures due to typos in fragment names,
 * the default constructor is annotated with [AlcubierreFragmentNameConstructor] prohibiting its use.
 * The safest way would be to use a class reference using a secondary constructor.
 *
 * In some multi-module architectures you may not have a class reference,
 * in that case use the `FragmentNameDialog` from module `alcubierre-render-fragment-screen-reflect`.
 * It allows to create a [FragmentDialog] with just a String name.
 *
 * @see FragmentCreator for manual Fragment instantiation.
 */
public abstract class FragmentDialog @AlcubierreFragmentNameConstructor constructor(
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
