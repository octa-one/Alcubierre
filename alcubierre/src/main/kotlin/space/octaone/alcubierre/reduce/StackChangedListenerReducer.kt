package space.octaone.alcubierre.reduce

import space.octaone.alcubierre.base.action.AnyNavAction
import space.octaone.alcubierre.base.reduce.LinkedNavReducer
import space.octaone.alcubierre.base.state.AnyRootNavState

/**
 * [space.octaone.alcubierre.base.reduce.NavReducer] for listening to stack selection.
 * Should be placed at the beginning of a reducers chain in order to observe changes made by the following reducers.
 *
 * @param onStackChanged Callback, invoked after selecting a new stack.
 */
public class StackChangedListenerReducer(
    private val onStackChanged: (from: Int, to: Int) -> Unit
): LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState {
        val newState = next.reduce(state, action)
        if (state.currentStackId != newState.currentStackId) {
            onStackChanged(state.currentStackId, newState.currentStackId)
        }
        return newState
    }
}
