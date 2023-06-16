package com.github.octaone.alcubierre

import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.tag
import com.github.octaone.alcubierre.state.RootNavState
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun <T : Screen> RootNavState<*, *>.findScreenByClass(kClass: KClass<T>): T? =
    currentStackState.chain.lastOrNull { kClass.isInstance(it) } as T?

inline fun <reified T : Screen> RootNavState<*, *>.findScreenByClass(): T? =
    findScreenByClass(T::class)

fun <S : Screen> RootNavState<S, *>.findScreenByTag(tag: String): S? =
    currentStackState.chain.lastOrNull { it.tag == tag }
