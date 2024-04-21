package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction

public interface NavReducer<S> {

    public fun reduce(state: S, action: AnyNavAction): S
}
