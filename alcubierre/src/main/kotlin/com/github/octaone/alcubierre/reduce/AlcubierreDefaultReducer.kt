package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.state.RootNavState

/**
 * Base reducers chain
 */
@Suppress("FunctionName")
fun AlcubierreDefaultNavReducer(): NavReducer<RootNavState> =
    DialogNavReducer(AlcubierreRootNavReducer())
