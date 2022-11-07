package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.state.RootNavState

/**
 * Базовая цепочка редьюсеров.
 */
@Suppress("FunctionName")
fun AlcubierreDefaultNavReducer(): NavReducer<RootNavState> =
    DialogNavReducer(AlcubierreRootNavReducer())
