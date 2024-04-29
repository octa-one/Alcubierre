@file:OptIn(AlcubierreInternalApi::class)

package com.github.octaone.alcubierre.render

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.github.octaone.alcubierre.NavDriveOwner
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.base.util.getParcelableCompat
import com.github.octaone.alcubierre.screen.ARG_DIALOG
import com.github.octaone.alcubierre.screen.FragmentCreator
import com.github.octaone.alcubierre.screen.FragmentDialog
import com.github.octaone.alcubierre.screen.withDialogData
import com.github.octaone.alcubierre.state.DialogNavState

/**
 * Render for [DialogNavState].
 *
 * @property classLoader [ClassLoader] for [FragmentFactory].
 * @property fragmentManager [FragmentManager] for [FragmentFactory].
 * @property onDismissRequest see [NavDriveOwner.requestDismissDialog]
 */
internal class AlcubierreDialogNavRender(
    private val classLoader: ClassLoader,
    private val fragmentManager: FragmentManager,
    private val onDismissRequest: () -> Unit
) : FragmentNavRender<DialogNavState<FragmentDialog>> {

    private var currentDialogId: String? = null

    // A lifecycle observer that reacts
    // if a dialog has been dismissed by a user interaction that bypasses NavDrive.
    private val dialogObserver = object : DefaultLifecycleObserver {

        override fun onStop(owner: LifecycleOwner) {
            val dialogFragment = owner as DialogFragment
            if (!dialogFragment.requireDialog().isShowing) {
                dialogFragment.setIsNotShowing()
                dialogFragment.lifecycle.removeObserver(this)
                currentDialogId = null
                onDismissRequest()
            }
        }
    }

    override fun render(state: DialogNavState<FragmentDialog>) {
        val newDialog = state.queue.firstOrNull()
        val newDialogId = newDialog?.dialogId
        if (currentDialogId == newDialogId) return

        // Need to dismiss the old Dialog.
        if (currentDialogId != null) {
            fragmentManager.findFragmentByTag(currentDialogId)
                ?.let { it as DialogFragment }
                ?.let { fragment ->
                    fragment.setIsNotShowing()
                    fragment.lifecycle.removeObserver(dialogObserver)
                    fragment.dismiss()
                }
        }
        // Show a new dialog and subscribe to its lifecycle.
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

    private fun DialogFragment.setIsNotShowing() {
        requireArguments().getParcelableCompat<FragmentDialog>(ARG_DIALOG)!!.isShowing = false
    }
}

private const val BUNDLE_KEY_DIALOG_STATE = "alc_dialog_render_state"
