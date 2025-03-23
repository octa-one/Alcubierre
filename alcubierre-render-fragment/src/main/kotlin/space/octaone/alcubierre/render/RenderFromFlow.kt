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

package space.octaone.alcubierre.render

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
