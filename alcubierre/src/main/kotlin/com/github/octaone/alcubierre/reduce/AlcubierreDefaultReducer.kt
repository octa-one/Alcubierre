package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.state.AnyRootNavState

/**
 * Base reducers chain
 */
@Suppress("FunctionName")
fun AlcubierreDefaultNavReducer(): NavReducer<AnyRootNavState> =
    AlcubierreRootNavReducer()
        .addReducer { origin -> DialogRootNavReducer(origin) }
        .addReducer { origin -> BatchRootNavReducer(origin) }
