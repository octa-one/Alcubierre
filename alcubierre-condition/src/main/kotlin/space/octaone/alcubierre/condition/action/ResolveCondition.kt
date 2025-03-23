package space.octaone.alcubierre.condition.action

import space.octaone.alcubierre.base.NavDrive
import space.octaone.alcubierre.base.action.NavAction
import space.octaone.alcubierre.base.screen.Dialog
import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.condition.ConditionalTarget
import space.octaone.alcubierre.condition.reducer.ConditionReducer

/**
 * Action to resolve a conditional target.
 * @see ConditionalTarget
 * @see ConditionReducer
 */
public data class ResolveCondition<S : Screen, D : Dialog>(val conditionalTarget: ConditionalTarget): NavAction<S, D>

/**
 * Extension to dispatch action [ResolveCondition].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.resolveCondition(conditionalTarget: ConditionalTarget): Unit =
    dispatch(ResolveCondition(conditionalTarget))
