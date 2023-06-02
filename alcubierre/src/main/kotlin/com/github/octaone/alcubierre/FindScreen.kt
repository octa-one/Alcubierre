package com.github.octaone.alcubierre

import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.tag
import com.github.octaone.alcubierre.state.RootNavState
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun <S : Screen, D : Dialog, T : Screen> RootNavState<S, D>.findScreenByClass(kClass: KClass<T>): T? =
    currentStackState.chain.lastOrNull { kClass.isInstance(it) } as T?

inline fun <S : Screen, D : Dialog, reified T : Screen> RootNavState<S, D>.findScreenByClass(): T? =
    findScreenByClass(T::class)

fun <S : Screen, D : Dialog> RootNavState<S, D>.findScreenByTag(tag: String): Screen? =
    currentStackState.chain.lastOrNull { it.tag == tag }
