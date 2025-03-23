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

package space.octaone.alcubierre.action

import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.base.state.RootNavState
import space.octaone.alcubierre.screen.tag
import kotlin.reflect.KClass

/**
 * Finds the [kClass] class screen in the current backstack.
 * @return Screen or null if it is not found.
 */
@Suppress("UNCHECKED_CAST")
public fun <S : Screen> RootNavState<*, *>.findScreenByClass(kClass: KClass<S>): S? =
    currentStackState.stack.lastOrNull { kClass.isInstance(it) } as S?

/**
 * Finds the [S] class screen in the current backstack.
 * @return Screen or null if it is not found.
 */
public inline fun <reified S : Screen> RootNavState<*, *>.findScreenByClass(): S? =
    findScreenByClass(S::class)

/**
 * Finds the screen tagged with [tag] in the current backstack.
 * @return Screen or null if it is not found.
 */
public fun <S : Screen> RootNavState<S, *>.findScreenByTag(tag: String): S? =
    currentStackState.stack.lastOrNull { it.tag == tag }
