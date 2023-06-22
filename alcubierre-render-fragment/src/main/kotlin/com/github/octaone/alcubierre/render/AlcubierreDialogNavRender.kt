package com.github.octaone.alcubierre.render

import android.os.Bundle
import androidx.fragment.app.DialogFragment
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
) : FragmentNavRender<DialogNavState<FragmentDialog>> {

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

    override fun render(state: DialogNavState<FragmentDialog>) {
        val newDialog = state.queue.firstOrNull()
        val newDialogId = newDialog?.dialogId
        if (currentDialogId == newDialogId) return

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
            val fragment = createFragment(newDialog).withDialogData(newDialog)
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

    private fun createFragment(dialog: FragmentDialog): DialogFragment {
        val fragment = if (dialog is FragmentCreator) {
            dialog.create()
        } else {
            fragmentManager.fragmentFactory.instantiate(classLoader, dialog.fragmentName)
        }
        return fragment as DialogFragment
    }
}

private const val BUNDLE_KEY_DIALOG_STATE = "alc_dialog_render_state"
