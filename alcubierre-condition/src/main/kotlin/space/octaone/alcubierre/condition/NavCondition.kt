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

package space.octaone.alcubierre.condition

import space.octaone.alcubierre.core.NavDrive
import space.octaone.alcubierre.core.screen.Dialog
import space.octaone.alcubierre.core.screen.Screen

public typealias AnyNavDrive = NavDrive<Screen, Dialog>

/**
 * An interface for classes that encapsulate conditional navigation logic.
 */
public interface NavCondition {

    /**
     * Condition resolving function.
     * @receiver A [NavDrive] that allows you to access current state and use action extensions.
     * @param target [ConditionalTarget] on which this [NavCondition] has been triggered.
     */
    public fun AnyNavDrive.resolve(target: ConditionalTarget)
}

/**
 * Factory for instantiating [NavCondition] from its class.
 * @see DefaultNavConditionFactory
 */
public interface NavConditionFactory {

    /**
     * Create a new instance of a [NavCondition].
     */
    public fun create(conditionalTarget: ConditionalTarget): NavCondition
}
