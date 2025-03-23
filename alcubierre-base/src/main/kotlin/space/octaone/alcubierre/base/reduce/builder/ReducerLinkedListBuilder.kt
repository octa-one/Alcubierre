@file:OptIn(AlcubierreInternalApi::class)

package space.octaone.alcubierre.base.reduce.builder

import space.octaone.alcubierre.base.action.AnyNavAction
import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.base.reduce.LinkedNavReducer
import space.octaone.alcubierre.base.reduce.NavReducer

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
