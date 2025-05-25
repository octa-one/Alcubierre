/*
 *  Copyright 2025. Alcubierre Contributors
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

package space.octaone.alcubierre.sample.condition

import space.octaone.alcubierre.action.forward
import space.octaone.alcubierre.condition.AnyNavDrive
import space.octaone.alcubierre.condition.ConditionalTarget
import space.octaone.alcubierre.condition.NavCondition
import space.octaone.alcubierre.sample.Counter
import space.octaone.alcubierre.sample.screen.SampleScreen
import javax.inject.Inject

class SampleConditionalTarget : ConditionalTarget(SampleNavCondition::class)

class SampleNavCondition @Inject constructor(
    private val toggle: Toggle
) : NavCondition {

    override fun AnyNavDrive.resolve(target: ConditionalTarget) {
        if (toggle.enabled()) {
            forward(SampleScreen(Counter.increment()))
        } else {
            null
        }
    }
}
