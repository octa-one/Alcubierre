package space.octaone.alcubierre.condition.reducer

import space.octaone.alcubierre.base.action.AnyNavAction
import space.octaone.alcubierre.base.reduce.LinkedNavReducer
import space.octaone.alcubierre.base.state.AnyRootNavState
import space.octaone.alcubierre.condition.NavCondition
import space.octaone.alcubierre.condition.NavConditionFactory
import space.octaone.alcubierre.condition.action.ResolveCondition

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
