package space.octaone.alcubierre.reduce

import space.octaone.alcubierre.action.Batch
import space.octaone.alcubierre.base.action.AnyNavAction
import space.octaone.alcubierre.base.reduce.LinkedNavReducer
import space.octaone.alcubierre.base.state.AnyRootNavState

/**
 * [space.octaone.alcubierre.base.reduce.NavReducer] for [Batch] action.
 * Reduce every recorded action from the head of the chain. Thus, Batch actions can be nested.
 */
public class BatchRootNavReducer : LinkedNavReducer<AnyRootNavState>() {

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState =
        when (action) {
            is Batch -> {
                action.actions.fold(state) { foldState, foldAction -> head.reduce(foldState, foldAction) }
            }
            else -> {
                next.reduce(state, action)
            }
        }
}
