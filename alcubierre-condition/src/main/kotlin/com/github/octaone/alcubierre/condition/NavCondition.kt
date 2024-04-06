package com.github.octaone.alcubierre.condition

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.state.AnyRootNavState

interface NavCondition {

    fun resolve(target: ConditionalTarget, state: AnyRootNavState): AnyNavAction
}

interface NavConditionFactory {

    fun create(conditionClass: Class<out NavCondition>): NavCondition
}
