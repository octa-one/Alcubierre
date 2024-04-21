package com.github.octaone.alcubierre.condition

public class DefaultNavConditionFactory : NavConditionFactory {

    override fun create(conditionClass: Class<out NavCondition>): NavCondition =
        conditionClass.getDeclaredConstructor().newInstance()
}
