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

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import space.octaone.alcubierre.ComposeNavDriveOwner
import space.octaone.alcubierre.base.NavDriveOwner

/**
 * Composable function that saves the state of [NavDriveOwner] with [rememberSaveable].
 *
 * It is not part of [AlcubierreRender] because you choose how to save its state.
 * Saving can be done with this function, or `Activity::onSaveInstanceState`, or `SavedStateHandle`, etc.
 */
@Composable
@NonRestartableComposable
public fun NavDriveOwnerSaver(navDriveOwner: ComposeNavDriveOwner) {
    val ownerSavedState = rememberSaveable { Bundle() }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        navDriveOwner.restoreState(ownerSavedState)

        val lifecycle = lifecycleOwner.lifecycle
        val observer = object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                navDriveOwner.saveState(ownerSavedState)
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}
