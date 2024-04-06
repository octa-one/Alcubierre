package com.github.octaone.alcubierre.condition

class DefaultNavConditionFactory : NavConditionFactory {

    override fun create(conditionClass: Class<out NavCondition>): NavCondition =
        conditionClass.getDeclaredConstructor().newInstance()
}
