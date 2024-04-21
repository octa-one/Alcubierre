package com.github.octaone.alcubierre.condition

import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

public abstract class ConditionalTarget(
    public val conditionClass: Class<out NavCondition>
) : ExtrasContainer by LazyExtrasContainer() {

    public constructor(conditionClass: KClass<out NavCondition>) : this(conditionClass.java)
}
