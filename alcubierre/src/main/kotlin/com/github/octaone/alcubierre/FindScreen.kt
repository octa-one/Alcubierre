package com.github.octaone.alcubierre

import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.tag
import com.github.octaone.alcubierre.state.RootNavState

inline fun <reified S : Screen> RootNavState.findScreen(): S? =
    currentStackState.chain.lastOrNull { it is S } as S?

fun RootNavState.findScreenById(id: String): Screen? =
    currentStackState.chain.lastOrNull { it.screenId == id }

fun RootNavState.findScreenByTag(tag: String): Screen? =
    currentStackState.chain.lastOrNull { it.tag == tag }
