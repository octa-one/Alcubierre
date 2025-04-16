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

@file:OptIn(AlcubierreFragmentNameConstructor::class)

package space.octaone.alcubierre.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import space.octaone.alcubierre.annotation.AlcubierreFragmentNameConstructor
import space.octaone.alcubierre.core.screen.Screen
import space.octaone.alcubierre.core.screen.extra.ExtrasContainer
import space.octaone.alcubierre.core.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

/**
 * [Screen] implementation based on [Fragment].
 * @param fragmentName Class of the fragment representing the screen.
 * @param replace Transaction to commit, replace or add.
 *
 * Android uses fragment names to instantiate fragments (see [FragmentFactory.instantiate]).
 * To prevent failures due to typos in fragment names,
 * the default constructor is annotated with [AlcubierreFragmentNameConstructor] prohibiting its use.
 * The safest way would be to use a class reference using a secondary constructor.
 *
 * In some multi-module architectures you may not have a class reference,
 * in that case use the `FragmentNameDialog` from module `alcubierre-render-fragment-screen-reflect`.
 * It allows to create a [FragmentScreen] with just a String name.
 *
 *
 * @see FragmentCreator for manual Fragment instantiation.
 */
public abstract class FragmentScreen @AlcubierreFragmentNameConstructor constructor(
    public val fragmentName: String,
    public val replace: Boolean
) : Screen(), ExtrasContainer by LazyExtrasContainer() {

    public constructor(
        fragmentClass: KClass<out Fragment>,
        replace: Boolean = true
    ) : this(fragmentClass.java.name, replace)
}
