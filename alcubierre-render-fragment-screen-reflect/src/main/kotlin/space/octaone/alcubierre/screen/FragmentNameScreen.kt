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
import space.octaone.alcubierre.annotation.AlcubierreFragmentNameConstructor

/**
 * “Unsafe” version of [FragmentScreen], allowing the fully qualified name to be used to reference the Fragment class.
 *
 * @param fragmentName Fully qualified name of the [Fragment] class.
 * @param replace Transaction to commit, replace or add.
 *
 * @see FragmentCreator for manual Fragment instantiation.
 */
public abstract class FragmentNameScreen(
    fragmentName: String,
    replace: Boolean = true
) : FragmentScreen(fragmentName, replace)
