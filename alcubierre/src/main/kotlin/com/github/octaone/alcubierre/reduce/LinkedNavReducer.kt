@file:Suppress("UNCHECKED_CAST")

package com.github.octaone.alcubierre.reduce

abstract class LinkedNavReducer<S> : NavReducer<S> {

    lateinit var head: NavReducer<S>
        internal set
    lateinit var next: NavReducer<S>
        internal set
}
