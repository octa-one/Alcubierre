package space.octaone.alcubierre.base.reduce.builder

import space.octaone.alcubierre.base.reduce.LinkedNavReducer
import space.octaone.alcubierre.base.reduce.NavReducer

/**
 * Similar to [listOf], but for Reducers.
 */
public fun <S> reducerLinkedListOf(vararg reducer: LinkedNavReducer<S>): NavReducer<S> {
    val builder = ReducerLinkedListBuilder<S>()
    reducer.forEach(builder::add)
    return builder.build()
}

/**
 * Similar to [buildList], but for Reducers.
 */
public inline fun <S> buildReducerLinkedList(action: ReducerLinkedListBuilder<S>.() -> Unit): NavReducer<S> =
    ReducerLinkedListBuilder<S>().apply(action).build()
