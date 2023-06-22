package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction

interface NavReducer<S> {

    fun reduce(state: S, action: AnyNavAction): S
}

inline fun <S> NavReducer<S>.addReducer(creator: (NavReducer<S>) -> NavReducer<S>): NavReducer<S> = creator(this)
