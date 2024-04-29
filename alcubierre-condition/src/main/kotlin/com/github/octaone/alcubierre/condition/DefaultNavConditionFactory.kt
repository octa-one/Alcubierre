package com.github.octaone.alcubierre.condition

/**
 * Simple implementation of [NavConditionFactory].
 * Uses Reflection Api to instantiate [NavCondition]s.
 */
public class DefaultNavConditionFactory : NavConditionFactory {

    override fun create(conditionClass: Class<out NavCondition>): NavCondition =
        conditionClass.getDeclaredConstructor().newInstance()
}
