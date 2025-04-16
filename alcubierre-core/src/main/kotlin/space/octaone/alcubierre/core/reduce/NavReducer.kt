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

package space.octaone.alcubierre.core.reduce

import space.octaone.alcubierre.core.action.AnyNavAction

/**
 * Interface for state update logic.
 */
public interface NavReducer<S> {

    /**
     * The reducer function that specifies how the state gets modified based on [action].
     * As an example, if we have a backstack state A - B, then Forward(C) action will reduce the state to A - B - C.
     *
     * @param state Current state.
     * @param action Action for reduce.
     * @return New state after applying [action] to [state].
     */
    public fun reduce(state: S, action: AnyNavAction): S
}
