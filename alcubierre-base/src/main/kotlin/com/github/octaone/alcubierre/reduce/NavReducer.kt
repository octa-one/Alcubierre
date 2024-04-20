package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction

interface NavReducer<S> {

    fun reduce(state: S, action: AnyNavAction): S
}
