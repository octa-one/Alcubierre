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
