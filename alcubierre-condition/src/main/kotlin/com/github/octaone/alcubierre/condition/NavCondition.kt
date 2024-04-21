package com.github.octaone.alcubierre.condition

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.state.AnyRootNavState

public interface NavCondition {

    public fun resolve(target: ConditionalTarget, state: AnyRootNavState): AnyNavAction
}

public interface NavConditionFactory {

    public fun create(conditionClass: Class<out NavCondition>): NavCondition
}
