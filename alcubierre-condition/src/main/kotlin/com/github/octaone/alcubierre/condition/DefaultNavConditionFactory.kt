package com.github.octaone.alcubierre.condition

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
