@file:OptIn(AlcubierreInternalApi::class)

package com.github.octaone.alcubierre.reduce

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.action.ApplyState
import com.github.octaone.alcubierre.action.Back
import com.github.octaone.alcubierre.action.BackTo
import com.github.octaone.alcubierre.action.BackToRoot
import com.github.octaone.alcubierre.action.ClearStack
import com.github.octaone.alcubierre.action.Forward
import com.github.octaone.alcubierre.action.NewStack
import com.github.octaone.alcubierre.action.Replace
import com.github.octaone.alcubierre.action.ReplaceRoot
import com.github.octaone.alcubierre.action.SelectStack
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.base.util.getNotNull
import com.github.octaone.alcubierre.state.AnyRootNavState
import com.github.octaone.alcubierre.state.AnyStackNavState
import com.github.octaone.alcubierre.state.StackNavState
import com.github.octaone.alcubierre.util.optimizeReadOnlyMap

/**
 * [NavReducer] responds for commands with stacks and retranslate remaining command to proper [stackReducer]
 */
public class ScreenRootNavReducer(
    private val stackReducer: NavReducer<AnyStackNavState> = AlcubierreStackNavReducer()
) : LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState = when (action) {
        is Forward, is Back, is Replace, is BackToRoot, is ReplaceRoot, is BackTo -> {
            state.modifyStack(state.currentStackId) { stackReducer.reduce(this, action) }
        }
        is NewStack -> {
            state.modifyStacks { put(action.stackId, StackNavState(action.screens)) }
        }
        is SelectStack -> {
            if (state.currentStackId == action.stackId) {
                state
            } else {
                check(state.stackStates.containsKey(action.stackId))
                state.copy(currentStackId = action.stackId)
            }
        }
        is ClearStack -> {
            if (state.stackStates.containsKey(action.stackId)) {
                check(state.currentStackId != action.stackId)
                state.modifyStacks { remove(action.stackId) }
            } else {
                state
            }
        }
        is ApplyState -> {
            action.state
        }
        else -> {
            state
        }
    }

    private inline fun AnyRootNavState.modifyStack(id: Int, update: AnyStackNavState.() -> AnyStackNavState): AnyRootNavState =
        modifyStacks { put(id, stackStates.getNotNull(id).update()) }

    private inline fun AnyRootNavState.modifyStacks(update: MutableMap<Int, AnyStackNavState>.() -> Unit): AnyRootNavState =
        copy(stackStates = HashMap(stackStates).apply(update).optimizeReadOnlyMap())
}
