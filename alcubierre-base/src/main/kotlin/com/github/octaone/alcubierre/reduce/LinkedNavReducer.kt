package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi

abstract class LinkedNavReducer<S> : NavReducer<S> {

    lateinit var head: NavReducer<S>
        @AlcubierreInternalApi set
    lateinit var next: NavReducer<S>
        @AlcubierreInternalApi set
}
