package com.github.octaone.alcubierre.render

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.github.octaone.alcubierre.ComposeNavDriveOwner
import com.github.octaone.alcubierre.NavDriveOwner

/**
 * Composable function that saves the state of [NavDriveOwner] with [rememberSaveable].
 *
 * It is not part of [AlcubierreRender] because you choose how to save its state.
 * Saving can be done with this function, or `Activity::onSaveInstanceState`, or `SavedStateHandle`, etc.
 */
@Composable
@NonRestartableComposable
public fun NavDriveOwnerSaver(navDriveOwner: ComposeNavDriveOwner) {
    val ownerSavedState = rememberSaveable { Bundle() }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        navDriveOwner.restoreState(ownerSavedState)

        val lifecycle = lifecycleOwner.lifecycle
        val observer = object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                navDriveOwner.saveState(ownerSavedState)
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}
