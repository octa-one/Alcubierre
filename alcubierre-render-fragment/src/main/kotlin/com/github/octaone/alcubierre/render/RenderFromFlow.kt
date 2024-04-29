package com.github.octaone.alcubierre.render

import android.os.Parcelable
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Renders the navigation state from [stateFlow] after [lifecycle] has been at least started.
 *
 * This is necessary because the default render commits transactions with [FragmentTransaction.commit],
 * which prevents the transaction from being executed after the state is saved.
 *
 * Quick notice: [FragmentTransaction.commitAllowingStateLoss] can be used,
 * but the default implementation of NavDriveOwner does the same thing.
 *
 * @param stateFlow The StateFlow emitting navigation state updates.
 * @param lifecycle The lifecycle of the Fragment or Activity where the renderer is created.
 */
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
