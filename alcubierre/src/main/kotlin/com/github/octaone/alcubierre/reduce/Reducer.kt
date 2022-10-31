package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.NavigationAction

interface NavigationReducer<S> {

    fun reduce(state: S, action: NavigationAction): S
}
