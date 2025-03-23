@file:OptIn(AlcubierreInternalApi::class)

package space.octaone.alcubierre.reduce

import space.octaone.alcubierre.action.ApplyState
import space.octaone.alcubierre.action.Back
import space.octaone.alcubierre.action.BackTo
import space.octaone.alcubierre.action.BackToRoot
import space.octaone.alcubierre.action.ClearStack
import space.octaone.alcubierre.action.Forward
import space.octaone.alcubierre.action.NewStack
import space.octaone.alcubierre.action.Replace
import space.octaone.alcubierre.action.ReplaceRoot
import space.octaone.alcubierre.action.SelectStack
import space.octaone.alcubierre.base.action.AnyNavAction
import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.base.reduce.LinkedNavReducer
import space.octaone.alcubierre.base.reduce.NavReducer
import space.octaone.alcubierre.base.state.AnyRootNavState
import space.octaone.alcubierre.base.state.AnyStackNavState
import space.octaone.alcubierre.base.state.RootNavState
import space.octaone.alcubierre.base.state.StackNavState
import space.octaone.alcubierre.base.util.getNotNull
import space.octaone.alcubierre.util.optimizeReadOnlyMap

/**
 * [space.octaone.alcubierre.base.reduce.NavReducer] for screen specific actions. Responsible for [RootNavState].
 * For actions associated with a particular stack, forwards them to [stackReducer],
 * then updates [RootNavState] with the updated [StackNavState] from [stackReducer].
 *
 * [NewStack] action creates a new stack in [RootNavState.stackStates].
 * [SelectStack] action changes value of [RootNavState.currentStackId].
 * Make sure that [SelectStack.stackId] exists in [RootNavState].
 * [ClearStack] action removes stack from [RootNavState].
 * Make sure you do not remove current [RootNavState.currentStackId] stack.
 * [ApplyState] action returns a completely new state from [ApplyState.state].
 *
 * @param stackReducer [space.octaone.alcubierre.base.reduce.NavReducer] that can reduce [StackNavState].
 */
public class ScreenRootNavReducer(
    private val stackReducer: NavReducer<AnyStackNavState> = ScreenStackNavReducer()
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
