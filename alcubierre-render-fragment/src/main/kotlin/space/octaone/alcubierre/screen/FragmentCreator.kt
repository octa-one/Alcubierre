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

import androidx.fragment.app.Fragment

/**
 * Fragments for [FragmentScreen] or [FragmentDialog] are created using FragmentFactory by their class name.
 * But sometimes it is necessary to create fragments manually.
 * For example, if the fragment is from an external library (MapFragment, etc).
 * In such cases, you can implement [FragmentCreator] in [FragmentScreen] or [FragmentDialog].
 * The default render will call it instead of FragmentFactory.
 */
public interface FragmentCreator {

    /**
     * Creates an instance of Fragment associated with [FragmentScreen] or [FragmentDialog].
     */
    public fun create(): Fragment
}
