package com.github.octaone.alcubierre.render

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.FragmentDialog
import com.github.octaone.alcubierre.screen.withDialogData

/**
 * Render для преобразования состояния диалога в команды [FragmentManager].
 *
 * @property onDismiss - колбек, чтобы сообщить о сворачивании диалога жестом не через библиотеку навигации.
 */
class AlcubierreDialogNavRender(
    private val classLoader: ClassLoader,
    private val fragmentManager: FragmentManager,
    private val onDismiss: () -> Unit
) : NavRender<Dialog?> {

    override var currentState: Dialog? = null

    private val dialogObserver = object : DefaultLifecycleObserver {

        override fun onStop(owner: LifecycleOwner) {
            val dialogFragment = owner as DialogFragment
            if (!dialogFragment.requireDialog().isShowing) {
                dialogFragment.lifecycle.removeObserver(this)
                currentState = null
                onDismiss()
            }
        }
    }

    override fun restoreState(state: Dialog?) {
        currentState = state
        if (state != null) {
            fragmentManager.findFragmentByTag(state.dialogId)
                .let(::requireNotNull)
                .lifecycle
                .addObserver(dialogObserver)
        }
    }

    override fun render(state: Dialog?) {
        if (currentState?.dialogId == state?.dialogId) return
        if (state != null) check(state is FragmentDialog) { "Unsupported dialog type $state" }

        val tempState = currentState
        // Если был старый диалог, его необходимо закрыть.
        if (tempState != null) {
            fragmentManager.findFragmentByTag(tempState.dialogId)
                ?.let { it as DialogFragment }
                ?.let { fragment ->
                    fragment.lifecycle.removeObserver(dialogObserver)
                    fragment.dismiss()
                }
        }
        // Если есть новый диалог, показываем его и подписываемся на закрытие.
        if (state != null) {
            state as FragmentDialog
            fragmentManager.fragmentFactory.instantiate(classLoader, state.fragmentName)
                .withDialogData(state)
                .also { fragment -> fragment.lifecycle.addObserver(dialogObserver) }
                .show(fragmentManager, state.dialogId)
        }
        currentState = state
    }
}
