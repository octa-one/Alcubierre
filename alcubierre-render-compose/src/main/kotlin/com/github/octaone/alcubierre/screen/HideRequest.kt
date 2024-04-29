package com.github.octaone.alcubierre.screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.Stable

/**
 * A class to register the effect of closing a dialog.
 *
 * A dialog can be closed in two ways:
 * 1) By user interaction, for example, by clicking outside the dialog.
 * This case is handled entirely by the dialog itself.
 * We only have to update the state in the onDismissRequest callback after the dialog is completely closed.
 * 2) On navigation action, in this case the state is changed without any interaction.
 * And to achieve graceful hiding with animation, we need to interact with the dialog state.
 * But since [ComposeDialog] can be represented by any kind of dialog,
 * you need to manually register an action to hide and wait for a hidden dialog state.
 *
 * It is also advised to write your own extensions and/or
 * base implementations of [ComposeDialog] for your specific dialogs (e.g. ModalBottomSheet, AlertDialog, etc.).
 */
@Stable
public class HideRequest(
    private val ownerName: String
) {

    private var action: (suspend () -> Unit)? = null

    public suspend fun hide() {
        action?.invoke()
            ?: Log.w(
                TAG,
                "HideRequest.HideEffect has not been registered " +
                        "for ${ownerName.substringAfterLast('.')}." +
                        "The dialog was immediately dismissed without a hiding animation."
            )
    }

    @Composable
    @NonRestartableComposable
    public fun HideEffect(action: suspend () -> Unit) {
        DisposableEffect(this) {
            this@HideRequest.action = action
            onDispose { this@HideRequest.action = null }
        }
    }
}

private const val TAG = "Alcubierre"
