package com.github.octaone.alcubierre.reduce.builder

import com.github.octaone.alcubierre.reduce.LinkedNavReducer
import com.github.octaone.alcubierre.reduce.NavReducer

fun <S> reducerLinkedListOf(vararg reducer: LinkedNavReducer<S>): NavReducer<S> {
    val builder = ReducerLinkedListBuilder<S>()
    reducer.forEach(builder::add)
    return builder.build()
}

inline fun <S> buildReducerLinkedList(action: ReducerLinkedListBuilder<S>.() -> Unit): NavReducer<S> =
    ReducerLinkedListBuilder<S>().apply(action).build()
