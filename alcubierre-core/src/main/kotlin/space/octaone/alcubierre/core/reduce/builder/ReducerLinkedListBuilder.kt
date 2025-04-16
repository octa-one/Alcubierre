/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(AlcubierreInternalApi::class)

package space.octaone.alcubierre.core.reduce.builder

import space.octaone.alcubierre.core.action.AnyNavAction
import space.octaone.alcubierre.core.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.core.reduce.LinkedNavReducer
import space.octaone.alcubierre.core.reduce.NavReducer

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
