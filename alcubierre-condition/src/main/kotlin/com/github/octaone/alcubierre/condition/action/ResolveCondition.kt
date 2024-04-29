package com.github.octaone.alcubierre.condition.action

import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.condition.ConditionalTarget
import com.github.octaone.alcubierre.condition.reducer.ConditionReducer
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

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
