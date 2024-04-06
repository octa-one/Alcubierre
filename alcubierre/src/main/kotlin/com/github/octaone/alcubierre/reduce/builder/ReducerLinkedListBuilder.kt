package com.github.octaone.alcubierre.reduce.builder

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.reduce.LinkedNavReducer
import com.github.octaone.alcubierre.reduce.NavReducer

class ReducerLinkedListBuilder<S> @PublishedApi internal constructor() {

    private val head = HeadNavReducer<S>()
    private var last: LinkedNavReducer<S> = head

    fun add(reducer: LinkedNavReducer<S>) {
        reducer.head = head
        last.next = reducer
        last = reducer
    }

    fun build(): LinkedNavReducer<S> {
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

fun <S> reducerLinkedListOf(vararg reducer: LinkedNavReducer<S>): NavReducer<S> {
    val builder = ReducerLinkedListBuilder<S>()
    reducer.forEach(builder::add)
    return builder.build()
}

inline fun <S> buildReducerLinkedList(action: ReducerLinkedListBuilder<S>.() -> Unit): NavReducer<S> =
    ReducerLinkedListBuilder<S>().apply(action).build()
