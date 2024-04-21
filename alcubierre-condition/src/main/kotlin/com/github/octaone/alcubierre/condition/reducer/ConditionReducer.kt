package com.github.octaone.alcubierre.condition.reducer

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.condition.NavConditionFactory
import com.github.octaone.alcubierre.condition.action.ResolveCondition
import com.github.octaone.alcubierre.reduce.LinkedNavReducer
import com.github.octaone.alcubierre.state.AnyRootNavState

public class ConditionReducer(
    private val conditionFactory: NavConditionFactory
) : LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState = when (action) {
        is ResolveCondition -> {
            runCatching {
                conditionFactory.create(action.conditionalTarget.conditionClass)
                    .resolve(action.conditionalTarget, state)
            }
                .getOrNull()
                ?.let { head.reduce(state, it) }
                ?: state
        }
        else -> {
            next.reduce(state, action)
        }
    }
}
