@file:OptIn(AlcubierreInternalApi::class)

package com.github.octaone.alcubierre.render

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.octaone.alcubierre.FragmentNavDriveOwner
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.base.util.getNotNull
import com.github.octaone.alcubierre.base.util.getParcelableArrayCompat
import com.github.octaone.alcubierre.render.modifier.EmptyModifier
import com.github.octaone.alcubierre.render.modifier.FragmentTransactionModifier
import com.github.octaone.alcubierre.screen.FragmentDialog
import com.github.octaone.alcubierre.screen.FragmentScreen
import com.github.octaone.alcubierre.state.DialogNavState
import com.github.octaone.alcubierre.state.FragmentDialogNavState
import com.github.octaone.alcubierre.state.FragmentRootNavState
import com.github.octaone.alcubierre.state.FragmentStackNavState
import com.github.octaone.alcubierre.state.RootNavState
import com.github.octaone.alcubierre.state.StackNavState

/**
 * Render for [FragmentRootNavState].
 * Calculates the difference between [RootNavState]s and calls the necessary [FragmentTransaction]s.
 *
 * A few notes on the behavior of the default implementation:
 * * [FragmentManager.saveBackStack]/[FragmentManager.restoreBackStack] API is used to manage multiple backstacks.
 * * Rendering consists from 4 steps:
 *   1. Dialogs are rendered with [dialogRender].
 *   2. BackStackEntries records are cleared if the corresponding fragments are no longer represented in the state.
 *   3. If the stack is changed, the current stack is saved and the destination stack is restored.
 *   4. Current stack is rendered with associated render from [stackRenders].
 * * If a fragment screen was replaced in the middle of the backstack,
 * all fragments above the modified screen will also be deleted and recreated.
 * This is because in the FragmentManager you cannot replace a fragment somewhere in the backstack.
 */
public open class AlcubierreRootNavRender(
    protected val containerId: Int,
    protected val classLoader: ClassLoader,
    protected val fragmentManager: FragmentManager,
    protected val navDriveOwner: FragmentNavDriveOwner,
    protected val transactionModifier: FragmentTransactionModifier = EmptyModifier
) : FragmentNavRender<FragmentRootNavState> {

    private var currentStackId: Int = -1
    private val stackRenders = HashMap<Int, RootIdAndStackRender>()
    private val dialogRender: FragmentNavRender<DialogNavState<FragmentDialog>> by lazy(LazyThreadSafetyMode.NONE) {
        createDialogRender()
    }

    /**
     * Creates [FragmentNavRender] for [FragmentStackNavState].
     * The root renderer will delegate the rendering of each stack to a separate stack renderer.
     */
    public open fun createStackRender(): FragmentNavRender<FragmentStackNavState> =
        AlcubierreStackNavRender(containerId, classLoader, fragmentManager, transactionModifier)

    /**
     * Creates [FragmentNavRender] for [FragmentDialogNavState].
     * The root renderer will delegate the rendering of dialogs to a dialog renderer.
     */
    public open fun createDialogRender(): FragmentNavRender<FragmentDialogNavState> =
        AlcubierreDialogNavRender(classLoader, fragmentManager, navDriveOwner::requestDismissDialog)

    override fun render(state: FragmentRootNavState) {
        // Apply dialog state changes.
        dialogRender.render(state.dialogState)

        val fromStackId = currentStackId
        val fromStackRootId = stackRenders[fromStackId]?.rootId
        val toStackId = state.currentStackId
        val toStackStateRootId = state.stackStates.getNotNull(toStackId).stack.firstOrNull()?.screenId

        if (fromStackId == toStackId) {
            // If current stack is not changed just call render of corresponding StackNavRender.
            clearUnusedStacks(fromStackId, state.stackStates)
            doRender(state)
        } else {
            // Save the old stack and restore the new stack if the stack has been changed.
            // Once the stack is restored, call the rendering of the corresponding StackNavRender.
            clearUnusedStacks(fromStackId, state.stackStates)
            fromStackRootId?.let { rootId ->
                fragmentManager.saveBackStack(rootId)
            }
            toStackStateRootId?.let { rootId ->
                fragmentManager.restoreBackStack(rootId)
            }

            doRender(state)
        }
    }

    override fun saveState(outState: Bundle) {
        dialogRender.saveState(outState)
        outState.putInt(BUNDLE_KEY_CURRENT_STACK, currentStackId)
        val stacksIterator = stackRenders.iterator()
        val stackBundles = Array(stackRenders.size) {
            val stackEntry = stacksIterator.next()
            Bundle().also { stackBundle ->
                stackBundle.putInt(BUNDLE_KEY_STACK_ID, stackEntry.key)
                stackBundle.putString(BUNDLE_KEY_STACK_ROOT, stackEntry.value.rootId)
                stackEntry.value.render.saveState(stackBundle)
            }
        }
        outState.putParcelableArray(BUNDLE_KEY_STACK_STATE, stackBundles)
    }

    override fun restoreState(savedState: Bundle?) {
        savedState ?: return
        dialogRender.restoreState(savedState)
        currentStackId = savedState.getInt(BUNDLE_KEY_CURRENT_STACK)

        stackRenders.clear()
        savedState.getParcelableArrayCompat<Bundle>(BUNDLE_KEY_STACK_STATE)?.forEach { stackBundle ->
            val stackId = stackBundle.getInt(BUNDLE_KEY_STACK_ID)
            stackRenders[stackId] = RootIdAndStackRender(
                rootId = stackBundle.getString(BUNDLE_KEY_STACK_ROOT)!!,
                render = createStackRender().also { render -> render.restoreState(stackBundle) }
            )
        }
    }

    private fun doRender(newState: FragmentRootNavState) {
        val toStackId = newState.currentStackId
        val toStackState = newState.stackStates.getNotNull(toStackId)
        if (toStackState.stack.isNotEmpty()) {
            val render = stackRenders[toStackId]?.render ?: createStackRender()

            render.render(toStackState)

            stackRenders[toStackId] = RootIdAndStackRender(
                rootId = toStackState.stack[0].screenId,
                render = render
            )
        }
        currentStackId = toStackId
    }

    /**
     * Method for finding and cleaning out old stacks.
     * This method is called to get rid of unnecessary fragments from the FragmentManager
     * in case where one stack has been completely replaced by another stack.
     */
    private fun clearUnusedStacks(fromStackId: Int, toStackStates: Map<Int, StackNavState<FragmentScreen>>) {
        val stacksIterator = stackRenders.iterator()
        // Iterate through stacks
        while (stacksIterator.hasNext()) {
            val (stackId, rootIdAndRender) = stacksIterator.next()
            // Get new state of existing stack
            val toStackState = toStackStates[stackId]
            if (toStackState == null || toStackState.stack.isEmpty() || toStackState.stack[0].screenId != rootIdAndRender.rootId) {
                stacksIterator.remove()
                popOrClearBackStack(rootIdAndRender.rootId, isSameStack = stackId == fromStackId)
            }
        }
    }

    private fun popOrClearBackStack(name: String, isSameStack: Boolean) {
        if (isSameStack) {
            fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else {
            fragmentManager.clearBackStack(name)
        }
    }

    private data class RootIdAndStackRender(
        val rootId: String,
        val render: FragmentNavRender<StackNavState<FragmentScreen>>
    )
}

private const val BUNDLE_KEY_CURRENT_STACK = "alc_root_current_stack"
private const val BUNDLE_KEY_STACK_ID = "alc_root_stack_id"
private const val BUNDLE_KEY_STACK_ROOT = "alc_root_stack_root"
private const val BUNDLE_KEY_STACK_STATE = "alc_root_stack_states"
