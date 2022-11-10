package com.github.octaone.alcubierre.state

import com.github.octaone.alcubierre.screen.Screen

inline fun <reified S : Screen> RootNavState.findScreen(): S? =
    currentStackState.chain.lastOrNull { it is S } as S?

inline fun <reified S : Screen> RootNavState.findScreen(id: String): S? =
    currentStackState.chain.lastOrNull { it.screenId == id && it is S } as S?
