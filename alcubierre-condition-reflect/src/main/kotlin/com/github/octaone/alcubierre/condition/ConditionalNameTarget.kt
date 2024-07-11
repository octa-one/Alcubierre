@file:OptIn(AlcubierreConditionalNameConstructor::class)

package com.github.octaone.alcubierre.condition

import com.github.octaone.alcubierre.condition.action.resolveCondition
import com.github.octaone.alcubierre.condition.annotation.AlcubierreConditionalNameConstructor
import com.github.octaone.alcubierre.condition.reducer.ConditionReducer

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
public abstract class ConditionalNameTarget(
    conditionName: String
) : ConditionalTarget(conditionName, null)
