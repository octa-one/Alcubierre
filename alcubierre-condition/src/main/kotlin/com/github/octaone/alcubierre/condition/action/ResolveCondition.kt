package com.github.octaone.alcubierre.condition.action

import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.condition.ConditionalTarget
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen

public data class ResolveCondition<S : Screen, D : Dialog>(val conditionalTarget: ConditionalTarget): NavAction<S, D>

public fun <S : Screen, D : Dialog> NavDrive<S, D>.resolveCondition(conditionalTarget: ConditionalTarget): Unit =
    dispatch(ResolveCondition(conditionalTarget))
