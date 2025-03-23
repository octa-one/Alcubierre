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

/**
 * Simple implementation of [NavConditionFactory].
 * Uses Reflection Api to instantiate [NavCondition]s.
 */
public open class DefaultNavConditionFactory(
    protected val classLoader: ClassLoader
) : NavConditionFactory {

    override fun create(conditionalTarget: ConditionalTarget): NavCondition =
        createInstance(createClass(classLoader, conditionalTarget))

    @Suppress("UNCHECKED_CAST")
    protected open fun createClass(classLoader: ClassLoader, conditionalTarget: ConditionalTarget): Class<out NavCondition> =
        conditionalTarget.conditionClass ?: Class.forName(requireNotNull(conditionalTarget.conditionName), false, classLoader) as Class<out NavCondition>

    protected open fun createInstance(conditionClass: Class<out NavCondition>): NavCondition =
        conditionClass.getDeclaredConstructor().newInstance()
}
