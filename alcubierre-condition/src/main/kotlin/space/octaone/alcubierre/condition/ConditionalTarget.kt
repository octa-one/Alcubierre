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

@file:OptIn(AlcubierreConditionalNameConstructor::class)

package space.octaone.alcubierre.condition

import space.octaone.alcubierre.core.screen.extra.ExtrasContainer
import space.octaone.alcubierre.core.screen.extra.LazyExtrasContainer
import space.octaone.alcubierre.condition.action.resolveCondition
import space.octaone.alcubierre.condition.annotation.AlcubierreConditionalNameConstructor
import space.octaone.alcubierre.condition.reducer.ConditionReducer
import kotlin.reflect.KClass

/**
 * A virtual target for conditional deeplink navigation.
 * Sometimes it is not possible to annotate a specific screen with Deeplink annotation,
 * because that screen might be disabled by a remote config or might be a part of an A/B test.
 * But you can replace the screen with ConditionalTarget and decide
 * what to do with the received link later inside the [conditionClass].
 *
 * You can also use this class to encapsulate any conditional navigation logic without using deeplinks.
 * Use the [resolveCondition] action to resolve the [ConditionalTarget] from your code.
 *
 * And don't forget to add [ConditionReducer] to the reducer chain!
 *
 * [NavCondition] class will be instantiated with [NavConditionFactory].
 * Default implementation [DefaultNavConditionFactory] uses Reflection API to call default constructor,
 * but you can create your own, for example to integrate with DI.
 *
 * Example:
 * ```
 * @Deeplink("myapp://sample/condition?id={id}")
 * class SampleConditionalTarget(
 *     val id: Int
 * ) : ConditionalTarget(SampleNavCondition::class)
 *
 * class SampleNavCondition : NavCondition {
 *
 *     override fun resolve(target: ConditionalTarget, state: AnyRootNavState): AnyNavAction {
 *         target as SampleConditionalTarget
 *         val targetScreen = if (target.id == 1) SampleScreen1() else SampleScreen2()
 *         return Forward(listOf(targetScreen))
 *     }
 * }
 * ```
 * In this example, target must be cast to a specific [ConditionalTarget] because
 * a single [NavCondition] can be used for multiple targets.
 */
public abstract class ConditionalTarget @AlcubierreConditionalNameConstructor constructor(
    public val conditionName: String?,
    public val conditionClass: Class<out NavCondition>?
) : ExtrasContainer by LazyExtrasContainer() {

    public constructor(conditionClass: KClass<out NavCondition>) : this(null, conditionClass.java)
}
