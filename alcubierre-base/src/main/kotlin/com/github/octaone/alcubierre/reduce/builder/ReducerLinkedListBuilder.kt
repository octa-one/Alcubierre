@file:OptIn(AlcubierreInternalApi::class)

package com.github.octaone.alcubierre.reduce.builder

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.reduce.LinkedNavReducer
import com.github.octaone.alcubierre.reduce.NavReducer

/**
 * Reducers chain (linked list) builder.
 * Allows to add new Reducers without the need of manually linking them to the added ones.
 */
public class ReducerLinkedListBuilder<S> @PublishedApi internal constructor() {

    private val head = HeadNavReducer<S>()
    private var last: LinkedNavReducer<S> = head

    /**
     * Adds new Reducer to the chain.
     */
    public fun add(reducer: LinkedNavReducer<S>) {
        reducer.head = head
        last.next = reducer
        last = reducer
    }

    /**
     * Completes the Reducers chain and returns the head.
     */
    public fun build(): LinkedNavReducer<S> {
        last.next = TailReducer()
        return head
    }

    private class HeadNavReducer<S> : LinkedNavReducer<S>() {

        init { head = this }

        override fun reduce(state: S, action: AnyNavAction): S =
            next.reduce(state, action)
    }

    private class TailReducer<S> : NavReducer<S> {

        override fun reduce(state: S, action: AnyNavAction): S = state
    }
}
