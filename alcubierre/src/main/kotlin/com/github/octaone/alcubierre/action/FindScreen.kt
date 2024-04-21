package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.tag
import com.github.octaone.alcubierre.state.RootNavState
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
public fun <T : Screen> RootNavState<*, *>.findScreenByClass(kClass: KClass<T>): T? =
    currentStackState.stack.lastOrNull { kClass.isInstance(it) } as T?

public inline fun <reified T : Screen> RootNavState<*, *>.findScreenByClass(): T? =
    findScreenByClass(T::class)

public fun <S : Screen> RootNavState<S, *>.findScreenByTag(tag: String): S? =
    currentStackState.stack.lastOrNull { it.tag == tag }
