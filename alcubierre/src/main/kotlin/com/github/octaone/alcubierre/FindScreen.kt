package com.github.octaone.alcubierre

import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.tag
import com.github.octaone.alcubierre.state.RootNavState
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun <S : Screen> RootNavState.findScreenByClass(kClass: KClass<S>): S? =
    currentStackState.chain.lastOrNull { kClass.isInstance(it) } as S?

inline fun <reified S : Screen> RootNavState.findScreenByClass(): S? =
    findScreenByClass(S::class)

fun RootNavState.findScreenByTag(tag: String): Screen? =
    currentStackState.chain.lastOrNull { it.tag == tag }
