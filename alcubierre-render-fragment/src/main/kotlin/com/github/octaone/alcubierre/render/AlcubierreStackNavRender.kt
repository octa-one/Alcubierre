package com.github.octaone.alcubierre.render

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.github.octaone.alcubierre.render.modifier.FragmentTransactionModifier
import com.github.octaone.alcubierre.screen.FragmentCreator
import com.github.octaone.alcubierre.screen.FragmentScreen
import com.github.octaone.alcubierre.screen.withScreenData
import com.github.octaone.alcubierre.state.StackNavState

/**
 * Render for [StackNavState].
 * Calculates the difference between states of the same stack and translates it into FragmentManager calls.
 *
 * A few notes on the behavior of the default implementation:
 * If a fragment screen was replaced in the middle of the backstack,
 * all fragments above the modified screen will also be deleted and recreated.
 * This is because in the FragmentManager you cannot replace a fragment somewhere in the backstack.
 */
internal class AlcubierreStackNavRender(
    private val containerId: Int,
    private val classLoader: ClassLoader,
    private val fragmentManager: FragmentManager,
    private val transactionModifier: FragmentTransactionModifier
) : FragmentNavRender<StackNavState<FragmentScreen>> {

    private var currentStack: List<String> = emptyList()

    override fun render(state: StackNavState<FragmentScreen>) {
        val diff = diff(currentStack, state.stack)
        diff.forEach { action ->
            when (action) {
                is Pop -> pop(action.count)
                is Push -> push(action.screens)
            }
        }
        currentStack = state.stack.map { it.screenId }
    }

    override fun saveState(outState: Bundle) {
        outState.putStringArray(BUNDLE_KEY_STACK_STATE, currentStack.toTypedArray())
    }

    override fun restoreState(savedState: Bundle?) {
        savedState ?: return
        currentStack = savedState.getStringArray(BUNDLE_KEY_STACK_STATE)?.toList().orEmpty()
    }

    /**
     * Pops backstack [count] times.
     */
    private fun pop(count: Int) {
        val entryName = currentStack[currentStack.size - count]
        fragmentManager.popBackStack(entryName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    /**
     * Performs replace or add transaction for each [screen][screens].
     */
    private fun push(screens: List<FragmentScreen>) {
        screens.forEach { screen ->
            val fragment = createFragment(screen).withScreenData(screen)
            fragmentManager.commit {
                setReorderingAllowed(true)
                transactionModifier.modify(this, screen, fragment)
                if (screen.replace) {
                    replace(containerId, fragment, screen.screenId)
                } else {
                    add(containerId, fragment, screen.screenId)
                }
                addToBackStack(screen.screenId)
            }
        }
    }

    private fun createFragment(screen: FragmentScreen): Fragment =
        if (screen is FragmentCreator) {
            screen.create()
        } else {
            fragmentManager.fragmentFactory.instantiate(classLoader, screen.fragmentName)
        }

    /**
     * Calculates diff of two stacks.
     * Comparison begins from the root.
     * When the first mismatch is detected the old stack [prev] is being popped
     * to this mismatched screen including itself and new stack of [next] is being pushed.
     */
    private fun diff(prev: List<String>, next: List<FragmentScreen>): List<StackAction> = when {
        prev.isEmpty() && next.isEmpty() -> emptyList()
        prev.isEmpty() -> listOf(Push(next))
        next.isEmpty() -> listOf(Pop(prev.size))
        else -> {
            var result: List<StackAction>? = null
            for (i in 0 until maxOf(prev.size, next.size)) {
                val p = prev.getOrNull(i)
                val n = next.getOrNull(i)?.screenId
                if (p == n) continue
                result = when {
                    p == null -> listOf(
                        Push(next.subList(i, next.size))
                    )
                    n == null -> listOf(
                        Pop(prev.size - i)
                    )
                    else -> listOf(
                        Pop(prev.size - i),
                        Push(next.subList(i, next.size))
                    )
                }
                break
            }
            result ?: emptyList()
        }
    }
}

private const val BUNDLE_KEY_STACK_STATE = "alc_stack_render_state"
