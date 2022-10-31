package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.state.RootNavigationState

/**
 * Базовая цепочка редьюсеров.
 */
@Suppress("FunctionName")
fun AlcubierreDefaultReducer(): NavigationReducer<RootNavigationState> =
    DialogNavigationReducer(AlcubierreRootNavigationReducer())
