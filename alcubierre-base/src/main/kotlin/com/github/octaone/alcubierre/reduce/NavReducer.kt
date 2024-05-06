package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction

/**
 * Interface for state update logic.
 */
public interface NavReducer<S> {

    /**
     * The reducer function that specifies how the state gets modified based on [action].
     * As an example, if we have a backstack state A - B, then Forward(C) action will reduce the state to A - B - C.
     *
     * @param state Current state.
     * @param action Action for reduce.
     * @return New state after applying [action] to [state].
     */
    public fun reduce(state: S, action: AnyNavAction): S
}
