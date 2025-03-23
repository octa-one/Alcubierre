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

package space.octaone.alcubierre.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * As stated in documentation for Lifecycle handling in Compose,
 * you cannot use `LifecycleEventEffect` to listen for Lifecycle.Event.ON_DESTROY
 * since composition ends before this signal is sent.
 *
 * That's why this class exists.
 * @property isFinishing will be set to false if the screen is destroyed, so you can check this at ON_STOP.
 * The idea is similar to `Activity.isFinishing` flag.
 */
public interface ScreenLifecycleOwner : LifecycleOwner {

    /**
     * Whether the screen is in the process of finishing and will be destroyed after ON_STOP.
     */
    public val isFinishing: Boolean
}

/**
 * Wrapper for [LocalLifecycleOwner] to get [ScreenLifecycleOwner] instead of a regular LifecycleOwner.
 */
public object LocalScreenLifecycleOwner {

    public val current: ScreenLifecycleOwner
        @ReadOnlyComposable
        @Composable
        get() = requireNotNull(LocalLifecycleOwner.current as? ScreenLifecycleOwner) {
            "No ScreenLifecycleOwner provided"
        }
}
