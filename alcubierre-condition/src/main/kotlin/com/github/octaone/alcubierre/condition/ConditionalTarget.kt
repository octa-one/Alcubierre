package com.github.octaone.alcubierre.condition

import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

abstract class ConditionalTarget(
    val conditionClass: Class<out NavCondition>
) : ExtrasContainer by LazyExtrasContainer() {

    constructor(conditionClass: KClass<out NavCondition>) : this(conditionClass.java)
}
