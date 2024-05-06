package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi

/**
 * Linked list implementation of [NavReducer].
 * The reduce starts at the head and traverses to the tail.
 *
 * Each reducer has access to the head to have an ability to restart the entire chain.
 * As an example, there's a chain:
 * HeadReducer -> ForwardReducer -> DeeplinkReducer
 * DeeplinkReducer waits for an action with the deeplink, processes it and triggers a chain of reducers with Forward action.
 * But there are no more reducers in the downstream, so, Forward reducing is impossible.
 * This is the case when it is safer to restart the chain from the head with a new action.
 */
public abstract class LinkedNavReducer<S> : NavReducer<S> {

    /**
     * First reducer in the chain.
     */
    public lateinit var head: NavReducer<S>
        @AlcubierreInternalApi set

    /**
     * Next reducer in the chain.
     */
    public lateinit var next: NavReducer<S>
        @AlcubierreInternalApi set
}
