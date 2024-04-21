package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi

public abstract class LinkedNavReducer<S> : NavReducer<S> {

    public lateinit var head: NavReducer<S>
        @AlcubierreInternalApi set
    public lateinit var next: NavReducer<S>
        @AlcubierreInternalApi set
}
