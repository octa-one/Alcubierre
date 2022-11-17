package com.github.octaone.alcubierre.render

import androidx.fragment.app.FragmentManager
import com.github.octaone.alcubierre.render.modifier.EmptyModifier
import com.github.octaone.alcubierre.render.modifier.FragmentTransactionModifier
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.state.RootNavState
import com.github.octaone.alcubierre.state.RootSavedState
import com.github.octaone.alcubierre.state.StackNavState
import com.github.octaone.alcubierre.util.getNotNull
import kotlin.properties.Delegates

class AlcubierreRootNavRender(
    private val containerId: Int,
    private val classLoader: ClassLoader,
    private val fragmentManager: FragmentManager,
    private val transactionModifier: FragmentTransactionModifier = EmptyModifier
) {

    var currentState: RootNavState = RootNavState.EMPTY

    private val stackRenders = HashMap<Int, NavRender<StackNavState>>()
    private var dialogRender: NavRender<Dialog?> by Delegates.notNull()

    fun render(state: RootNavState) {
        // Apply dialog state changes
        dialogRender.render(state.dialog)
        
        // If current stack is not appeared so it is reasonable to skip remaining stacks render because of its invisibility for the user
        if (isStacksEquals(currentState, state)) {
            clearUnusedStacks(currentState, state)
            currentState = state
            return
        }

        val fromStackId = currentState.currentStackId
        val fromStackState = currentState.stacks.getNotNull(fromStackId)
        val toStackId = state.currentStackId
        val toStackState = state.stacks.getNotNull(toStackId)

        if (fromStackId == toStackId) {
            // If current stack is not changed just call render of corresponding StackNavRender
            clearUnusedStacks(currentState, state)

            doRender(state)
        } else {
            // Save old stack and restore new one if stack is changed
            // After restoring of stack call render of corresponding StackNavRender
            clearUnusedStacks(currentState, state)
            fromStackState.chain.firstOrNull()?.let { root ->
                fragmentManager.saveBackStack(root.screenId)
            }
            toStackState.chain.firstOrNull()?.let { root ->
                fragmentManager.restoreBackStack(root.screenId)
            }

            doRender(state)
        }
    }

    fun restoreState(state: RootSavedState) {
        dialogRender.restoreState(state.state.dialog)
        state.rendered.forEach { (id, stackState) ->
            stackRenders[id] = createStackRender()
                .also { render -> render.restoreState(stackState) }
        }
    }

    fun saveState(): RootSavedState =
        RootSavedState(
            state = currentState,
            rendered = stackRenders.mapValues { it.value.currentState }
        )

    internal fun setOnDialogDismissed(onDialogDismissed: () -> Unit) {
        dialogRender = AlcubierreDialogNavRender(classLoader, fragmentManager, onDialogDismissed)
    }

    private fun createStackRender(): NavRender<StackNavState> =
        AlcubierreStackNavRender(containerId, classLoader, fragmentManager, transactionModifier)

    private fun doRender(newState: RootNavState) {
        val toStackId = newState.currentStackId
        val toStackState = newState.stacks.getNotNull(toStackId)

        val render = stackRenders.getOrPut(toStackId) { createStackRender() }
        render.render(toStackState)

        currentState = newState
    }

    private fun isStacksEquals(oldState: RootNavState, newState: RootNavState): Boolean {
        if (oldState.currentStackId != newState.currentStackId) return false
        if (oldState.currentStackState != newState.currentStackState) return false
        return true
    }

    /**
     * Method for searching and cleaтing of old stacks
     * This method is called to get rid of unneccessary fragments from FragmentManager in case of one stack was fully changed by another stack
     */
    private fun clearUnusedStacks(from: RootNavState, to: RootNavState) {
        from.stacks.forEach { (stackId, stack) ->
            val toStack = to.stacks[stackId]
            if (toStack != null) {
                // If [to] contains stack with [stackId] need to check root screen.
                // For FragmentManager it is a new stack so need to clear the old one
                stack.chain.firstOrNull()
                    ?.takeIf { root -> root.screenId != toStack.chain.firstOrNull()?.screenId }
                    ?.let { root ->
                        stackRenders[stackId] = createStackRender()
                        fragmentManager.clearBackStack(root.screenId)
                    }
            } else {
                // If [to] doesn't contain [stackId] it is unneccessary to have it in FragmentManager
                stackRenders.remove(stackId)
                stack.chain.firstOrNull()?.let { root ->
                    fragmentManager.clearBackStack(root.screenId)
                }
            }
        }
    }
}
