package com.github.octaone.alcubierre.render

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.github.octaone.alcubierre.render.modifier.EmptyModifier
import com.github.octaone.alcubierre.render.modifier.FragmentTransactionModifier
import com.github.octaone.alcubierre.screen.ScreenId
import com.github.octaone.alcubierre.state.DialogNavState
import com.github.octaone.alcubierre.state.RootNavState
import com.github.octaone.alcubierre.state.StackNavState
import com.github.octaone.alcubierre.util.getNotNull
import com.github.octaone.alcubierre.util.getParcelableArrayCompat
import kotlin.properties.Delegates

class AlcubierreRootNavRender(
    private val containerId: Int,
    private val classLoader: ClassLoader,
    private val fragmentManager: FragmentManager,
    private val transactionModifier: FragmentTransactionModifier = EmptyModifier
) : NavRender<RootNavState> {

    private var currentStackId: Int = -1
    private val stacks = HashMap<Int, RootIdAndStackRender>()
    private var dialogRender: NavRender<DialogNavState> by Delegates.notNull()

    override fun render(state: RootNavState) {
        // Apply dialog state changes
        dialogRender.render(state.dialogState)

        val fromStackId = currentStackId
        val fromStackRootId = stacks[fromStackId]?.rootId
        val toStackId = state.currentStackId
        val toStackStateRootId = state.stackStates.getNotNull(toStackId).chain.firstOrNull()?.screenId

        if (fromStackId == toStackId) {
            // If current stack is not changed just call render of corresponding StackNavRender
            clearUnusedStacks(state)
            doRender(state)
        } else {
            // Save old stack and restore new one if stack is changed
            // After restoring of stack call render of corresponding StackNavRender
            clearUnusedStacks(state)
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
        val stacksIterator = stacks.iterator()
        val stackBundles = Array(stacks.size) {
            val stackEntry = stacksIterator.next()
            Bundle().also { stackBundle ->
                stackBundle.putInt(BUNDLE_KEY_STACK_ID, stackEntry.key)
                stackBundle.putString(BUNDLE_KEY_STACK_ROOT, stackEntry.value.rootId)
                stackEntry.value.render.saveState(stackBundle)
            }
        }
        outState.putParcelableArray(BUNDLE_KEY_STACK_STATE, stackBundles)
    }

    override fun restoreState(bundle: Bundle) {
        dialogRender.restoreState(bundle)
        currentStackId = bundle.getInt(BUNDLE_KEY_CURRENT_STACK)

        stacks.clear()
        bundle.getParcelableArrayCompat<Bundle>(BUNDLE_KEY_STACK_STATE)?.forEach { stackBundle ->
            val stackId = stackBundle.getInt(BUNDLE_KEY_STACK_ID)
            stacks[stackId] = RootIdAndStackRender(
                rootId = stackBundle.getString(BUNDLE_KEY_STACK_ROOT),
                render = createStackRender().also { render -> render.restoreState(stackBundle) }
            )
        }
    }

    internal fun setOnDialogDismissed(onDialogDismissed: () -> Unit) {
        dialogRender = AlcubierreDialogNavRender(classLoader, fragmentManager, onDialogDismissed)
    }

    private fun createStackRender(): NavRender<StackNavState> =
        AlcubierreStackNavRender(containerId, classLoader, fragmentManager, transactionModifier)

    private fun doRender(newState: RootNavState) {
        val toStackId = newState.currentStackId
        val toStackState = newState.stackStates.getNotNull(toStackId)

        val render = stacks[toStackId]?.render ?: createStackRender()

        render.render(toStackState)

        stacks[toStackId] = RootIdAndStackRender(
            rootId = toStackState.chain.firstOrNull()?.screenId,
            render = render
        )
        currentStackId = toStackId
    }

    /**
     * Method for searching and clearing of old stacks
     * This method is called to get rid of unnecessary fragments from FragmentManager in case of one stack was fully changed by another stack
     */
    private fun clearUnusedStacks(newState: RootNavState) {
        val stacksIterator = stacks.iterator()
        // Iterate through stacks
        while (stacksIterator.hasNext()) {
            val (stackId, stack) = stacksIterator.next()
            // Get new state of existing stack
            val toStack = newState.stackStates[stackId]
            if (toStack != null) {
                val newRootId = toStack.chain.firstOrNull()?.screenId
                // If [newState] contains stack with [stackId] need to check root screen.
                // If the root screen has changed, we must recreate the stack.
                if (stack.rootId != null && stack.rootId != newRootId) {
                    stacks[stackId] = RootIdAndStackRender(newRootId, createStackRender())
                    fragmentManager.clearBackStack(stack.rootId)
                }
            } else {
                // If [newState] doesn't contain [stackId] it is unnecessary to have it in FragmentManager
                stacksIterator.remove()
                if (stack.rootId != null) {
                    fragmentManager.clearBackStack(stack.rootId)
                }
            }
        }
    }

    private data class RootIdAndStackRender(
        val rootId: ScreenId?,
        val render: NavRender<StackNavState>
    )
}

private const val BUNDLE_KEY_CURRENT_STACK = "alc_root_current_stack"
private const val BUNDLE_KEY_STACK_ID = "alc_root_stack_id"
private const val BUNDLE_KEY_STACK_ROOT = "alc_root_stack_root"
private const val BUNDLE_KEY_STACK_STATE = "alc_root_stack_states"
