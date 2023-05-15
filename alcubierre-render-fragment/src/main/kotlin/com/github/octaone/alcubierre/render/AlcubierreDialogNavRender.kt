package com.github.octaone.alcubierre.render

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.github.octaone.alcubierre.screen.FragmentCreator
import com.github.octaone.alcubierre.screen.FragmentDialog
import com.github.octaone.alcubierre.screen.isShowing
import com.github.octaone.alcubierre.screen.withDialogData
import com.github.octaone.alcubierre.state.DialogNavState

/**
 * Render for mapping DialogState to [FragmentManager] commands
 *
 * @property onDismiss - callback provides messages about dialogState dismiss by gesture avoiding reducers
 */
class AlcubierreDialogNavRender(
    private val classLoader: ClassLoader,
    private val fragmentManager: FragmentManager,
    private val onDismiss: () -> Unit
) : FragmentNavRender<DialogNavState> {

    private var currentDialogId: String? = null

    private val dialogObserver = object : DefaultLifecycleObserver {

        override fun onStop(owner: LifecycleOwner) {
            val dialogFragment = owner as DialogFragment
            if (!dialogFragment.requireDialog().isShowing) {
                dialogFragment.lifecycle.removeObserver(this)
                currentDialogId = null
                onDismiss()
            }
        }
    }

    override fun render(state: DialogNavState) {
        val newDialog = state.queue.firstOrNull()
        val newDialogId = newDialog?.dialogId
        if (currentDialogId == newDialogId) return
        if (newDialog != null) check(newDialog is FragmentDialog) { "Unsupported dialogState type $state" }

        // Need to dismiss old dialogState
        if (currentDialogId != null) {
            fragmentManager.findFragmentByTag(currentDialogId)
                ?.let { it as DialogFragment }
                ?.let { fragment ->
                    fragment.lifecycle.removeObserver(dialogObserver)
                    fragment.dismiss()
                }
        }
        // Show new dialog and subscribe for closing
        if (newDialog != null) {
            newDialog as FragmentDialog
            val fragment = (createFragment(newDialog) as DialogFragment).withDialogData(newDialog)
            fragment.lifecycle.addObserver(dialogObserver)
            fragment.show(fragmentManager, newDialogId)

            newDialog.isShowing = true
        }
        currentDialogId = newDialogId
    }

    override fun saveState(outState: Bundle) {
        outState.putString(BUNDLE_KEY_DIALOG_STATE, currentDialogId)
    }

    override fun restoreState(savedState: Bundle?) {
        savedState ?: return
        val restoredDialogId = savedState.getString(BUNDLE_KEY_DIALOG_STATE)
        currentDialogId = restoredDialogId
        if (restoredDialogId != null) {
            fragmentManager.findFragmentByTag(restoredDialogId)
                .let(::requireNotNull)
                .lifecycle
                .addObserver(dialogObserver)
        }
    }

    private fun createFragment(dialog: FragmentDialog): Fragment =
        if (dialog is FragmentCreator) {
            dialog.create()
        } else {
            fragmentManager.fragmentFactory.instantiate(classLoader, dialog.fragmentName)
        }
}

private const val BUNDLE_KEY_DIALOG_STATE = "alc_dialog_render_state"
