package com.github.octaone.alcubierre.render

import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public fun <T : Parcelable> FragmentNavRender<T>.renderFrom(stateFlow: StateFlow<T>, lifecycle: Lifecycle) {
    val lifecycleScope = lifecycle.coroutineScope
    var lastState: T? = null
    stateFlow
        .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
        .onEach { state ->
            if (state !== lastState) {
                lastState = state
                render(state)
            }
        }
        .launchIn(lifecycleScope)
}
