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

@file:Suppress("LocalVariableName")

package space.octaone.alcubierre.deeplink.processor.api

/**
 * Base class for a converter from a set of placeholders to an actual navigation target.
 * Implementations will be generated with Alcubierre deeplink processor.
 * As an example,
 * mapOf("id" to 1, "action" to "SayHello") can be converter to ComposeScreen(id = 1, action = "SayHello").
 */
public interface ScreenConverter {

    /**
     * @param _from the set of placeholders obtained by mapping a deeplink to [Deeplink.pattern].
     *
     * @return target of matched Deeplink annotation.
     * It can be Screen, Dialog, ConditionTarget or any custom processor-supported classes.
     * @throws Exception if string values of placeholders cannot be converted to parameter types of the class being converted.
     */
    public fun convert(_from: Map<String, String>): Any
}
