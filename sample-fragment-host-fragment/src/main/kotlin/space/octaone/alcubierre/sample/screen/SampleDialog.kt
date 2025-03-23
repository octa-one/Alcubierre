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

package space.octaone.alcubierre.sample.screen

import kotlinx.parcelize.Parcelize
import space.octaone.alcubierre.sample.fragment.SampleDialogFragment
import space.octaone.alcubierre.screen.FragmentDialog

@Parcelize
data class SampleDialog(
    val someId: Int,
    override val priority: Int
) : FragmentDialog(SampleDialogFragment::class)
