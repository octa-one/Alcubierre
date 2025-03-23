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

package space.octaone.alcubierre.base.state

import space.octaone.alcubierre.base.screen.Dialog
import space.octaone.alcubierre.base.screen.Screen

public class StackStateBuilder<S : Screen>  {

    private val screens = mutableListOf<S>()

    public fun screen(screen: S) {
        screens.add(screen)
    }

    public fun build(): StackNavState<S> =
        StackNavState(screens.toList())
}

public class RootStateBuilder<S : Screen, D : Dialog> @PublishedApi internal constructor() {

    private val stacks = HashMap<Int, StackNavState<S>>()
    private var stackId: Int? = null

    public inline fun stack(id: Int, builder: StackStateBuilder<S>.() -> Unit) {
        stack(id, StackStateBuilder<S>().apply(builder).build())
    }

    @PublishedApi
    internal fun stack(id: Int, state: StackNavState<S>) {
        if (stackId == null) stackId = id
        stacks[id] = state
    }

    public fun selectStack(id: Int) {
        stackId = id
    }

    public fun build(): RootNavState<S, D> =
        RootNavState(DialogNavState.EMPTY, stacks, checkNotNull(stackId))
}

/**
 * DSL for creating [RootNavState].
 *
 * Example:
 * ```
 * rootState {
 *     stack(1) {
 *         screen(SampleScreen1())
 *     }
 *     stack(2) {
 *         screen(SampleScreen2())
 *         screen(SampleScreen3())
 *     }
 * }
 * ```
 */
public inline fun <S : Screen, D : Dialog> rootState(builder: RootStateBuilder<S, D>.() -> Unit): RootNavState<S, D> =
    RootStateBuilder<S, D>().apply(builder).build()

/**
 * DSL for creating [RootNavState] without multi backstack.
 * All screens will be in the same stack with id [SINGLE_STACK_ID].
 *
 * Example:
 * ```
 * singleStackRootState {
 *     screen(SampleScreen1())
 *     screen(SampleScreen2())
 *     screen(SampleScreen3())
 * }
 * ```
 */
public inline fun <S : Screen, D : Dialog> singleStackRootState(
    stackId: Int = SINGLE_STACK_ID,
    builder: StackStateBuilder<S>.() -> Unit
): RootNavState<S, D> =
    RootStateBuilder<S, D>()
        .apply { stack(stackId, builder) }
        .build()

/**
 * Stack id, used in [singleStackRootState].
 */
public const val SINGLE_STACK_ID: Int = 100
