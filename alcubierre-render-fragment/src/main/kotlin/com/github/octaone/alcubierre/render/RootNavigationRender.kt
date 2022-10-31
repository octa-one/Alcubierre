package com.github.octaone.alcubierre.render

import com.github.octaone.alcubierre.render.modifier.EmptyModifier
import com.github.octaone.alcubierre.render.modifier.TransactionModifier
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.state.RootSavedState
import com.github.octaone.alcubierre.state.RootNavigationState
import com.github.octaone.alcubierre.state.StackNavigationState
import com.github.octaone.alcubierre.util.getNotNull
import androidx.fragment.app.FragmentManager
import kotlin.properties.Delegates

class AlcubierreRootNavigationRender(
    private val containerId: Int,
    private val classLoader: ClassLoader,
    private val fragmentManager: FragmentManager,
    private val transactionModifier: TransactionModifier = EmptyModifier
) {

    var currentState: RootNavigationState = RootNavigationState.EMPTY

    private val stackRenders = HashMap<Int, Render<StackNavigationState>>()
    private var dialogRender: Render<Dialog?> by Delegates.notNull()

    internal fun setOnDialogDismissed(onDialogDismissed: () -> Unit) {
        dialogRender = AlcubierreDialogNavigationRender(classLoader, fragmentManager, onDialogDismissed)
    }

    private fun createStackRender(): Render<StackNavigationState> =
        AlcubierreStackNavigationRender(containerId, classLoader, fragmentManager, transactionModifier)

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

    fun render(state: RootNavigationState) {
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
            // Если текущий стек не поменялся, то только вызываем render у соответствующего StackNavigationRender.
            clearUnusedStacks(currentState, state)

            doRender(state)
        } else {
            // Если стек поменялся, то сохраняем старый стек и восстанавлвиаем новый.
            // После восстановления стека вызываем render у соответствующего StackNavigationRender.
            clearUnusedStacks(currentState, state)
            fromStackState.chain.firstOrNull()?.let { root ->
                fragmentManager.saveBackStack(root.id)
            }
            toStackState.chain.firstOrNull()?.let { root ->
                fragmentManager.restoreBackStack(root.id)
            }

            doRender(state)
        }
    }

    private fun doRender(newState: RootNavigationState) {
        val toStackId = newState.currentStackId
        val toStackState = newState.stacks.getNotNull(toStackId)

        val render = stackRenders.getOrPut(toStackId) { createStackRender() }
        render.render(toStackState)

        currentState = newState
    }

    private fun isStacksEquals(oldState: RootNavigationState, newState: RootNavigationState): Boolean {
        if (oldState.currentStackId != newState.currentStackId) return false
        if (oldState.currentStackState != newState.currentStackState) return false
        return true
    }

    /**
     * Метод для поиска и очистки неактуальных стеков.
     * Например был стек авторизации, который потом заменился стеками фичей, чтобы не оставалось лишних фрагментов в FragmentManager, вызывается этот метод.
     */
    private fun clearUnusedStacks(from: RootNavigationState, to: RootNavigationState) {
        from.stacks.forEach { (stackId, stack) ->
            val toStack = to.stacks[stackId]
            if (toStack != null) {
                // Если в [to] есть стек [stackId], проверяем root экран, потому что если он изменился,
                // то с точки зрения FragmentManager это новый стек, а значит старый надо очистить.
                stack.chain.firstOrNull()
                    ?.takeIf { root -> root.id != toStack.chain.firstOrNull()?.id }
                    ?.let { root ->
                        stackRenders[stackId] = createStackRender()
                        // localRenders[stackId]?.currentState = StackNavigationState.EMPTY
                        fragmentManager.clearBackStack(root.id)
                    }
            } else {
                // Если в [to] нет стека [stackId], значит он больше не нужен в FragmentManager.
                stackRenders.remove(stackId)
                stack.chain.firstOrNull()?.let { root ->
                    fragmentManager.clearBackStack(root.id)
                }
            }
        }
    }
}
