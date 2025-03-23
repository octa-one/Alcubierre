/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(AlcubierreInternalApi::class)

package space.octaone.alcubierre.render

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import space.octaone.alcubierre.base.NavDriveOwner
import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.base.state.DialogNavState
import space.octaone.alcubierre.base.util.getParcelableCompat
import space.octaone.alcubierre.screen.ARG_DIALOG
import space.octaone.alcubierre.screen.FragmentCreator
import space.octaone.alcubierre.screen.FragmentDialog
import space.octaone.alcubierre.screen.withDialogData

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
            newDialog.isShowing = true
            fragment.lifecycle.addObserver(dialogObserver)
            fragment.show(fragmentManager, newDialogId)
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
            val fragment = requireNotNull(fragmentManager.findFragmentByTag(restoredDialogId))
            fragment.lifecycle.addObserver(dialogObserver)
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
