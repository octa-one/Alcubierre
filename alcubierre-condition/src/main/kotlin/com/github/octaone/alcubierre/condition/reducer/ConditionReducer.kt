package com.github.octaone.alcubierre.condition.reducer

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.condition.NavCondition
import com.github.octaone.alcubierre.condition.NavConditionFactory
import com.github.octaone.alcubierre.condition.action.ResolveCondition
import com.github.octaone.alcubierre.reduce.LinkedNavReducer
import com.github.octaone.alcubierre.state.AnyRootNavState

/**
 * Reducer for handling [ResolveCondition] actions.
 * Uses [NavConditionFactory] to create a [NavCondition]
 * and calls [NavCondition.resolve] to get the final action to be performed.
 *
 * Be aware: resolved action will be reduced from the head or chain reducers.
 * So it doesn't matter if [ConditionReducer] is at the end or the beginning of the chain.
 * But it's better to place it at the beginning to minimize unnecessary reduce calls.
 */
public class ConditionReducer(
    private val conditionFactory: NavConditionFactory
) : LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState = when (action) {
        is ResolveCondition -> {
            conditionFactory.create(action.conditionalTarget)
                .resolve(action.conditionalTarget, state)
                ?.let { head.reduce(state, it) }
                ?: state
        }
        else -> {
            next.reduce(state, action)
        }
    }
}
