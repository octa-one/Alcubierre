package com.github.octaone.alcubierre.render

import com.github.octaone.alcubierre.render.modifier.FragmentTransactionModifier
import com.github.octaone.alcubierre.screen.FragmentCreator
import com.github.octaone.alcubierre.screen.FragmentScreen
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.withScreenData
import com.github.octaone.alcubierre.state.StackNavState
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

/**
 * Render для преобразования [StackNavState] в команды [FragmentManager].
 */
class AlcubierreStackNavRender(
    private val containerId: Int,
    private val classLoader: ClassLoader,
    private val fragmentManager: FragmentManager,
    private val transactionModifier: FragmentTransactionModifier
) : NavRender<StackNavState> {

    override var currentState: StackNavState = StackNavState.EMPTY

    override fun restoreState(state: StackNavState) {
        currentState = state
    }

    override fun render(state: StackNavState) {
        val diff = diff(currentState, state)
        diff.forEach { action ->
            when (action) {
                is Pop -> {
                    pop(action.count)
                }
                is Push -> {
                    push(action.screens)
                }
            }
        }
        currentState = state
    }

    /**
     * Выполняет popBackStack на число фрагментов [count].
     */
    private fun pop(count: Int) {
        val entryIndex = fragmentManager.backStackEntryCount - count
        if (entryIndex !in 0 until fragmentManager.backStackEntryCount) return
        val entryName = fragmentManager.getBackStackEntryAt(entryIndex).name
        fragmentManager.popBackStack(entryName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    /**
     * Выполняет replace или add фрагментов из [screens].
     */
    private fun push(screens: List<Screen>) {
        screens.forEach { screen ->
            when (screen) {
                is FragmentScreen -> {
                    fragmentManager.commit {
                        setReorderingAllowed(true)
                        val fragment = createFragment(screen)
                        transactionModifier.modify(this, screen, fragment)
                        if (screen.replace) {
                            replace(containerId, fragment, screen.screenId)
                        } else {
                            add(containerId, fragment, screen.screenId)
                        }
                        addToBackStack(screen.screenId)
                    }
                }
                else -> {
                    throw IllegalArgumentException("Unsupported screen type $screen")
                }
            }
        }
    }

    private fun createFragment(screen: FragmentScreen): Fragment =
        if (screen is FragmentCreator) {
            screen.create()
        } else {
            fragmentManager.fragmentFactory.instantiate(classLoader, screen.fragmentName).withScreenData(screen)
        }

    /**
     * Метод для определения разницы двух стеков.
     * Сравнение начинается с корня, при первом несовпадении
     * старый стек [prev] "попается" до этого экрана включительно и "пушится" новый стек из [next].
     */
    private fun diff(prev: StackNavState, next: StackNavState): List<StackAction> = when {
        prev.chain.isEmpty() && next.chain.isEmpty() -> emptyList()
        prev.chain.isEmpty() -> listOf(Push(next.chain))
        next.chain.isEmpty() -> listOf(Pop(prev.chain.size))
        else -> {
            var result: List<StackAction>? = null
            for (i in 0 until maxOf(prev.chain.size, next.chain.size)) {
                val p = prev.chain.getOrNull(i)?.screenId
                val n = next.chain.getOrNull(i)?.screenId
                if (p == n) continue
                result = when {
                    p == null -> listOf(
                        Push(next.chain.subList(i, next.chain.size))
                    )
                    n == null -> listOf(
                        Pop(prev.chain.size - i)
                    )
                    else -> listOf(
                        Pop(prev.chain.size - i),
                        Push(next.chain.subList(i, next.chain.size))
                    )
                }
                break
            }
            result ?: emptyList()
        }
    }
}
