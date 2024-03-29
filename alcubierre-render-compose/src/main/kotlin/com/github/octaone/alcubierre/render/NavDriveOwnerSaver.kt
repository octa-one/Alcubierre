package com.github.octaone.alcubierre.render

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.github.octaone.alcubierre.ComposeNavDriveOwner

@Composable
@NonRestartableComposable
fun NavDriveOwnerSaver(navDriveOwner: ComposeNavDriveOwner) {
    val ownerSavedState = rememberSaveable { Bundle() }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
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
