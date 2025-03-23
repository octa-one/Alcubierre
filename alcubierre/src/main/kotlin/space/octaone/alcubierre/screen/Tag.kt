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

package space.octaone.alcubierre.screen

import space.octaone.alcubierre.action.findScreenByTag
import space.octaone.alcubierre.base.screen.extra.ExtrasContainer

/**
 * A simple extension to associate a screen or dialog with a string as a tag.
 * Can be used to find a screen in the backstack.
 * @see findScreenByTag
 */
public var ExtrasContainer.tag: String?
    get() = if (hasExtras()) {
        extras.getString(TAG)
    } else {
        null
    }
    set(value) {
        if (value != null) {
            extras.putString(TAG, value)
        } else {
            extras.remove(TAG)
        }
    }

private const val TAG = "space.octaone.alcubierre.TAG"
