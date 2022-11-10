package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.NavAction

interface NavReducer<S> {

    fun reduce(state: S, action: NavAction): S
}
