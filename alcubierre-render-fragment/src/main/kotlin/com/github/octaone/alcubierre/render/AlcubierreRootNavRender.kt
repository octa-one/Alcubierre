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
        // Применяем изменения состояния диалога.
        dialogRender.render(state.dialog)

        // Если текущий стек не поменялся, то можно пропустить рендер остальных стеков, потому что они всё равно не видны пользователю.
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
            // Если текущий стек не поменялся, то только вызываем render у соответствующего StackNavRender.
            clearUnusedStacks(currentState, state)

            doRender(state)
        } else {
            // Если стек поменялся, то сохраняем старый стек и восстанавлвиаем новый.
            // После восстановления стека вызываем render у соответствующего StackNavRender.
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
     * Метод для поиска и очистки неактуальных стеков.
     * Например был стек авторизации, который потом заменился стеками фичей, чтобы не оставалось лишних фрагментов в FragmentManager, вызывается этот метод.
     */
    private fun clearUnusedStacks(from: RootNavState, to: RootNavState) {
        from.stacks.forEach { (stackId, stack) ->
            val toStack = to.stacks[stackId]
            if (toStack != null) {
                // Если в [to] есть стек [stackId], проверяем root экран, потому что если он изменился,
                // то с точки зрения FragmentManager это новый стек, а значит старый надо очистить.
                stack.chain.firstOrNull()
                    ?.takeIf { root -> root.screenId != toStack.chain.firstOrNull()?.screenId }
                    ?.let { root ->
                        stackRenders[stackId] = createStackRender()
                        fragmentManager.clearBackStack(root.screenId)
                    }
            } else {
                // Если в [to] нет стека [stackId], значит он больше не нужен в FragmentManager.
                stackRenders.remove(stackId)
                stack.chain.firstOrNull()?.let { root ->
                    fragmentManager.clearBackStack(root.screenId)
                }
            }
        }
    }
}
